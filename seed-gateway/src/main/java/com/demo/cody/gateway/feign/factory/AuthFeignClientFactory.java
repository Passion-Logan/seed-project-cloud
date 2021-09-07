package com.demo.cody.gateway.feign.factory;

import com.demo.cody.gateway.feign.fallback.AuthFeignClientFallback;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author wql
 * @desc AuthFeignClientFactory
 * @date 2021/9/7
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/9/7
 */
@Component
public class AuthFeignClientFactory implements FallbackFactory<AuthFeignClientFallback> {
    @Override
    public AuthFeignClientFallback create(Throwable throwable) {
        return new AuthFeignClientFallback(throwable);
    }
}
