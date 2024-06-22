package com.example.encryptservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EncryptServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EncryptServiceApplication.class, args);
    }

}
