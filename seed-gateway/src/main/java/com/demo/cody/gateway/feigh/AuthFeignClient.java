package com.demo.cody.gateway.feigh;

import com.demo.cody.gateway.feigh.fallback.AuthFeignClientFallback;
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
@FeignClient(name = "seed-auth", fallback = AuthFeignClientFallback.class)
public interface AuthFeignClient {

    @GetMapping("/oauth/hasPermission")
    boolean permission(@RequestParam String authentication, @RequestParam String url, @RequestParam String method);

}
