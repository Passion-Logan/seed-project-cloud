package com.demo.cody.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * ClassName: SeedAuthApplication
 *
 * @author WQL
 * @Description:
 * @date: 2021/7/11 16:15
 * @since JDK 1.8
 */
@EnableDiscoveryClient
@SpringBootApplication
public class SeedAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeedAuthApplication.class, args);
    }

}
