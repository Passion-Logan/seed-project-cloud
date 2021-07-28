package com.demo.cody.gateway.feigh.fallback;

import com.demo.cody.gateway.feigh.AuthFeignClient;
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
@Component
public class AuthFeignClientFallback implements AuthFeignClient {
    @Override
    public boolean permission(String authentication, String url, String method) {
        log.error("Feign调用接口失败:{}，method:{}", url, method);
        return false;
    }
}
