package com.nttdata.banking.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import reactor.core.publisher.Hooks;

/**
 * Customer Service Application.
 * 
 * ComponentScan includes:
 * - com.nttdata.banking.customer: Application code
 * - com.nttdata.banking.shared: Shared library (JWT, security, etc.)
 */
@SpringBootApplication
@ComponentScan(basePackages = {
        "com.nttdata.banking.customer",
        "com.nttdata.banking.shared"
})
public class CustomerServiceApplication {

    public static void main(String[] args) {
        // Enable reactor debugging in development
        Hooks.enableAutomaticContextPropagation();

        SpringApplication.run(CustomerServiceApplication.class, args);
    }
}