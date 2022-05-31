package ru.example.sword.tradeshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.config.EnableIntegration;

@SpringBootApplication
@EnableIntegration
public class TradeshopApplication {
    public static void main(String[] args) {
        SpringApplication.run(TradeshopApplication.class, args);
    }
}
