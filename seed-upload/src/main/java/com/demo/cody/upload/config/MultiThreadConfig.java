package com.demo.cody.upload.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ForkJoinPool;

/**
 * @author wql
 * @desc 文件处理线程池
 * @date 2021/10/8
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/10/8
 */
@Slf4j
@Configuration
public class MultiThreadConfig {

    @Bean(destroyMethod = "shutdown")
    public ForkJoinPool fileForkJoinPool() {
        int poolSize = Math.max(Runtime.getRuntime().availableProcessors() * 2, 8);
        log.info("" + poolSize);
        return new ForkJoinPool(poolSize);
    }

}
