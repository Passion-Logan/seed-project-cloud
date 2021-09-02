package com.demo.cody.auth.config;

import cn.hutool.core.thread.NamedThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 异步线程池配置
 *
 * @author wql
 * @desc AsyncConfiguration
 * @date 2021/9/2
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/9/2
 */
@Configuration
public class AsyncConfiguration {

    /**
     * 线程池最小值
     */
    private static final int MIN_SIZE = 5;

    /**
     * 拒绝策略: 当线程池满之后, 将抛出异常.
     */
    @Bean(name = "defaultThreadPoolExecutor", destroyMethod = "shutdown")
    public ThreadPoolExecutor buildDefaultThreadPoolExecutor() {
        // 核心线程数 = cpu * 2 ; maxPoolSize = 核心线程数 * 2
        int corePoolSize = Math.max(MIN_SIZE, Runtime.getRuntime().availableProcessors() << 1);
        return new ThreadPoolExecutor(corePoolSize,
                corePoolSize << 1,
                60L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(800),
                new NamedThreadFactory("seed-default-thread", false),
                (r, executor) -> {
                    throw new RuntimeException("程序运行错误");
                });
    }

}
