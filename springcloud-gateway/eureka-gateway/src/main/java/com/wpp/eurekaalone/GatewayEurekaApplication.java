package com.wpp.eurekaalone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class GatewayEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayEurekaApplication.class, args);
    }

}
