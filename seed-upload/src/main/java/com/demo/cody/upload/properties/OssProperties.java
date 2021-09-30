package com.demo.cody.upload.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wql
 * @desc OssProperties
 * @date 2021/9/29
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/9/29
 */
@Data
@ConfigurationProperties(prefix = OssProperties.PREFIX)
public class OssProperties {

    public static final String PREFIX = "seed.oss";

    /**
     * oss 服务地址相关信息
     */
    private String endpoint;
    /**
     * 用户AccessKey
     */
    private String accessKey;
    /**
     * accessKeySecret
     */
    private String secretKey;
    /**
     * 储存目的地名称
     */
    private String bucketName;
    /**
     * 静态域名
     */
    private String staticDomain;


}
