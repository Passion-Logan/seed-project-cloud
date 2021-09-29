package com.demo.cody.upload.config.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.demo.cody.upload.properties.OssProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wql
 * @desc OssConfiguration
 * @date 2021/9/29
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/9/29
 */
@Configuration
@ConditionalOnClass(name = "com.aliyun.oss.OSS")
@EnableConfigurationProperties(OssConfiguration.class)
public class OssConfiguration {

    /**
     * 创建 OSS 客户端
     *
     * @return OSS
     */
    @Bean
    @ConditionalOnClass(OSS.class)
    public OSS getOssClient(OssProperties ossProperties) {
        return new OSSClientBuilder().build(ossProperties.getEndpoint(), ossProperties.getAccessKey(), ossProperties.getSecretKey());
    }

}
