package com.demo.cody.auth.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author wql
 * @desc SecurityConfigProperties
 * @date 2021/7/27
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/7/27
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.security.oauth2")
public class SecurityConfigProperties {

    private String clientId;
    private String clientSecret;
    private String scope;

}
