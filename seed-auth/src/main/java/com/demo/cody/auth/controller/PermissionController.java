package com.demo.cody.auth.controller;

import com.demo.cody.auth.service.IAuthService;
import com.demo.cody.common.vo.Result;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author wql
 * @desc PermissionController
 * @date 2021/7/27
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/7/27
 */
@RestController
@RequestMapping("permission")
public class PermissionController {

    @Resource
    private IAuthService authService;

    /**
     * 用户鉴权
     *
     * @param jwtToken jwtToken
     * @return Jws<Claims>
     */
    @GetMapping("getJwt")
    public Result<Jws<Claims>> getJwt(String jwtToken) {
        return Result.ok(authService.getJwt(jwtToken));
    }

}
