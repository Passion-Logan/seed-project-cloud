package com.demo.cody.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

/**
 * ClassName: GatewayApplication
 *
 * @author WQL
 * @Description:
 * @date: 2021/7/11 15:16
 * @since JDK 1.8
 */
@SpringBootApplication(scanBasePackages = {"com.demo.cody"})
public class SeedSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeedSystemApplication.class, args);
    }
}
