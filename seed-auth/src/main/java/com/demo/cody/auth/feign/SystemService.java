package com.demo.cody.auth.feign;

import com.demo.cody.auth.feign.factory.SystemFallbackFactory;
import com.demo.cody.core.utils.response.Result;
import com.demo.cody.model.entity.SysLoginLog;
import com.demo.cody.model.entity.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
     * @return Boolean
     */
    @PostMapping("/auth/insertLog")
    Result<Boolean> insertLog(@RequestBody SysLoginLog data);

    /**
     * 根据用户名查找用户
     *
     * @param userName 用户名
     * @return SysUser SysUser
     */
    @GetMapping("/sys/user/getByUserName")
    SysUser findByUsername(@RequestParam("userName") String userName);

    /**
     * 根据用户id查询用户权限
     *
     * @param userId userId
     * @return String
     */
    @GetMapping("/sys/menu/getPermissionsByUserId")
    List<String> getPermissionsByUserId(@RequestParam("userId") String userId);


}
