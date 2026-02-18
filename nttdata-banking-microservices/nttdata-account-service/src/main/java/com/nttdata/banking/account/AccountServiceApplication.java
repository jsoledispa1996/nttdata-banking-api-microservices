package com.nttdata.banking.account;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;


@Slf4j
@SpringBootApplication(scanBasePackages = {
        "com.nttdata.banking.account",
        "com.nttdata.banking.shared"
})
@EnableR2dbcRepositories(basePackages = "com.nttdata.banking.account.domain.repository")
public class AccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);

    }
}
