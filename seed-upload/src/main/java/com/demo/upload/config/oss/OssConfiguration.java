package com.demo.upload.config.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.demo.upload.properties.OssProperties;
import com.demo.upload.service.FileService;
import com.demo.upload.service.impl.OssServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
@EnableConfigurationProperties(OssProperties.class)
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

    @Bean
    @ConditionalOnBean({OSS.class})
    @ConditionalOnMissingBean(FileService.class)
    public FileService getOssService(OssProperties ossProperties, OSS ossClient) {
        return new OssServiceImpl(ossProperties, ossClient);
    }

}
