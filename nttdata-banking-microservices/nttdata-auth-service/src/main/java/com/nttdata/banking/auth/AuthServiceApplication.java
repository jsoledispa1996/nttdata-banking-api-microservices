package com.nttdata.banking.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;


@SpringBootApplication(
        exclude = {
                DataSourceAutoConfiguration.class,
                R2dbcAutoConfiguration.class
        },
        scanBasePackages = {
                "com.nttdata.banking.auth",
                "com.nttdata.banking.shared"
        }
)
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
}
