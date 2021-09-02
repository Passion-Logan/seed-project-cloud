package com.demo.cody.auth.feign;

import com.demo.cody.auth.feign.factory.SystemFallbackFactory;
import com.demo.cody.common.entity.SysLoginLog;
import com.demo.cody.common.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 远程调用系统接口
 *
 * @author wql
 * @date 2021/9/2
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/9/2
 */
@FeignClient(contextId = "systemService", value = "seed-system", fallbackFactory = SystemFallbackFactory.class)
public interface SystemService {

    /**
     * 日志记录
     *
     * @param data data
     * @return
     */
    @PostMapping("/auth/insertLog")
    Result<Boolean> insertLog(@RequestBody SysLoginLog data);

}
