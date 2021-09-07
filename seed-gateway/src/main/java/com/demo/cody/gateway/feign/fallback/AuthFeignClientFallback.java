package com.demo.cody.gateway.feign.fallback;

import com.demo.cody.gateway.feign.AuthFeignClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author wql
 * @desc AuthFeignClientFallback
 * @date 2021/7/28
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/7/28
 */
@Slf4j
@AllArgsConstructor
public class AuthFeignClientFallback implements AuthFeignClient {

    /**
     * 错误
     */
    private final Throwable throwable;

    @Override
    public boolean permission(String authentication, String url, String method) {
        log.error("Feign调用接口失败:{}，method:{}, 信息:{}", url, method, throwable);
        return false;
    }
}
