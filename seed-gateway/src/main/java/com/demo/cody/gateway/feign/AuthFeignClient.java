package com.demo.cody.gateway.feign;

import com.demo.cody.gateway.feign.factory.AuthFeignClientFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 鉴权客户端
 *
 * @author wql
 * @desc AuthFeignClient
 * @date 2021/7/28
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/7/28
 */
@FeignClient(contextId = "authFeignClient", value = "seed-auth", fallbackFactory = AuthFeignClientFactory.class)
public interface AuthFeignClient {

    @GetMapping("/oauth/hasPermission")
    boolean permission(@RequestParam String authentication, @RequestParam String url, @RequestParam String method);

}
