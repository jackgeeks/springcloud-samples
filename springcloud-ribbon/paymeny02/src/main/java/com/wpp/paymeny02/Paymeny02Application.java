package com.wpp.paymeny02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Paymeny02Application {

    public static void main(String[] args) {
        SpringApplication.run(Paymeny02Application.class, args);
    }

}
