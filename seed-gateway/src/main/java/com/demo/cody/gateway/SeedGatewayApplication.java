package com.demo.cody.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * ClassName: GatewayApplication
 *
 * @author WQL
 * @Description:
 * @date: 2021/7/11 15:16
 * @since JDK 1.8
 */
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class SeedGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeedGatewayApplication.class, args);
    }
}
