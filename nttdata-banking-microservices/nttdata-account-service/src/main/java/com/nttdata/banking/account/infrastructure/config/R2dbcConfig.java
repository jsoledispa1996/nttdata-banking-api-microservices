package com.nttdata.banking.account.infrastructure.config;

import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Slf4j
@Configuration
@EnableTransactionManagement
public class R2dbcConfig extends AbstractR2dbcConfiguration {

    private final ConnectionFactory connectionFactory;

    public R2dbcConfig(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
        log.info("Configuración R2DBC inicializada");
    }

    @Override
    public ConnectionFactory connectionFactory() {
        return this.connectionFactory;
    }
}
