package com.demo.cody.auth.feign.factory;

import com.demo.cody.auth.feign.SystemService;
import com.demo.cody.auth.feign.fallback.SystemServiceImpl;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author wql
 * @desc SystemFallbackFactory
 * @date 2021/9/2
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/9/2
 */
@Component
public class SystemFallbackFactory implements FallbackFactory<SystemService> {

    @Override
    public SystemService create(Throwable throwable) {
        return new SystemServiceImpl(throwable);
    }

}
