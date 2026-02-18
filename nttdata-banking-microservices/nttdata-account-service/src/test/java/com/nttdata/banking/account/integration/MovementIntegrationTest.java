package com.nttdata.banking.account.integration;

import com.nttdata.banking.account.domain.model.Account;
import com.nttdata.banking.account.domain.model.AccountType;
import com.nttdata.banking.account.domain.model.Movement;
import com.nttdata.banking.account.domain.repository.AccountRepository;
import com.nttdata.banking.account.domain.repository.MovementRepository;
import com.nttdata.banking.account.infrastructure.rest.dto.CreateMovementRequest;
import com.nttdata.banking.account.integration.config.TestSecurityConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Testcontainers
@Import(TestSecurityConfig.class)
class MovementIntegrationTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass")
            .withInitScript("schema.sql");

    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.0"));

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.r2dbc.url", () -> String.format(
                "r2dbc:mysql://%s:%d/%s",
                mysql.getHost(),
                mysql.getFirstMappedPort(),
                mysql.getDatabaseName()
        ));
        registry.add("spring.r2dbc.username", mysql::getUsername);
        registry.add("spring.r2dbc.password", mysql::getPassword);


        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MovementRepository movementRepository;

    private Account testAccount;

    @BeforeEach
    void setUp() {
        // Clean database before each test
        try {
            movementRepository.deleteAll().block();
            accountRepository.deleteAll().block();
        } catch (Exception e) {
            // Ignore if tables don't exist yet (first run)
        }

        // Create test account
        testAccount = Account.builder()
                .accountNumber("478758")
                .accountType(AccountType.AHORRO)
                .initialBalance(new BigDecimal("2000.00"))
                .currentBalance(new BigDecimal("2000.00"))
                .active(true)
                .customerId("CUST001")
                .createdDate(LocalDateTime.now())
                .build();

        testAccount = accountRepository.save(testAccount).block();
    }

    @AfterEach
    void tearDown() {
        // Clean database after each test
        try {
            movementRepository.deleteAll().block();
            accountRepository.deleteAll().block();
        } catch (Exception e) {
            // Ignore cleanup errors
        }
    }


    @Test
    void testCreateMovement_Deposit_CompleteFlow() {

        CreateMovementRequest depositRequest = CreateMovementRequest.builder()
                .accountNumber("478758")
                .movementType("DEPOSITO")
                .amount(new BigDecimal("600.00"))
                .description("Integration test deposit")
                .build();


        webTestClient.post()
                .uri("/api/v1/movements")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(depositRequest)
                .exchange()
                // Then - Verify HTTP response
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.movementType").isEqualTo("DEPOSITO")
                .jsonPath("$.amount").isEqualTo(600.00)
                .jsonPath("$.balance").isEqualTo(2600.00)
                .jsonPath("$.accountNumber").isEqualTo("478758")
                .jsonPath("$.description").isEqualTo("Integration test deposit");


        Account updatedAccount = accountRepository.findByAccountNumber("478758").block();
        assertThat(updatedAccount).isNotNull();
        assertThat(updatedAccount.getCurrentBalance()).isEqualByComparingTo(new BigDecimal("2600.00"));


        Movement savedMovement = movementRepository.findByAccountIdOrderByMovementDateDesc(testAccount.getId())
                .blockFirst();
        assertThat(savedMovement).isNotNull();
        assertThat(savedMovement.getAmount()).isEqualByComparingTo(new BigDecimal("600.00"));
        assertThat(savedMovement.getBalance()).isEqualByComparingTo(new BigDecimal("2600.00"));
    }


    @Test
    void testCreateMovement_InsufficientBalance_ReturnsError() {

        CreateMovementRequest withdrawalRequest = CreateMovementRequest.builder()
                .accountNumber("478758")
                .movementType("RETIRO")
                .amount(new BigDecimal("3000.00"))
                .description("Withdrawal exceeding balance")
                .build();


        webTestClient.post()
                .uri("/api/v1/movements")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(withdrawalRequest)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.mensaje").value(message ->
                        assertThat(message.toString()).contains("Saldo no disponible")
                );


        Account unchangedAccount = accountRepository.findByAccountNumber("478758").block();
        assertThat(unchangedAccount).isNotNull();
        assertThat(unchangedAccount.getCurrentBalance()).isEqualByComparingTo(new BigDecimal("2000.00"));


        Long movementCount = movementRepository.findByAccountIdOrderByMovementDateDesc(testAccount.getId())
                .count()
                .block();
        assertThat(movementCount).isEqualTo(0L);
    }

    @Test
    void testMultipleMovements_BalanceUpdatesCorrectly() {
        CreateMovementRequest deposit1 = CreateMovementRequest.builder()
                .accountNumber("478758")
                .movementType("DEPOSITO")
                .amount(new BigDecimal("500.00"))
                .description("First deposit")
                .build();

        CreateMovementRequest withdrawal = CreateMovementRequest.builder()
                .accountNumber("478758")
                .movementType("RETIRO")
                .amount(new BigDecimal("300.00"))
                .description("Withdrawal")
                .build();

        CreateMovementRequest deposit2 = CreateMovementRequest.builder()
                .accountNumber("478758")
                .movementType("DEPOSITO")
                .amount(new BigDecimal("100.00"))
                .description("Second deposit")
                .build();


        webTestClient.post()
                .uri("/api/v1/movements")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(deposit1)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.balance").isEqualTo(2500.00);


        webTestClient.post()
                .uri("/api/v1/movements")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(withdrawal)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.balance").isEqualTo(2200.00);


        webTestClient.post()
                .uri("/api/v1/movements")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(deposit2)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.balance").isEqualTo(2300.00);


        Account finalAccount = accountRepository.findByAccountNumber("478758").block();
        assertThat(finalAccount).isNotNull();
        assertThat(finalAccount.getCurrentBalance()).isEqualByComparingTo(new BigDecimal("2300.00"));


        Long movementCount = movementRepository.findByAccountIdOrderByMovementDateDesc(testAccount.getId())
                .count()
                .block();
        assertThat(movementCount).isEqualTo(3L);
    }
}
