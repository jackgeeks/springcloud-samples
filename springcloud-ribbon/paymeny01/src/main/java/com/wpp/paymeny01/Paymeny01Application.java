package com.wpp.paymeny01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Paymeny01Application {
    public static void main(String[] args) {
        SpringApplication.run(Paymeny01Application.class, args);
    }

}
