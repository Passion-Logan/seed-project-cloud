package com.demo.cody.auth.controller;

import com.demo.cody.auth.service.IAuthService;
import com.demo.cody.common.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    private IAuthService authService;

    /**
     * 用户鉴权
     *
     * @param jwtToken
     * @return
     */
    @GetMapping("getJwt")
    public Result getJwt(String jwtToken) {
        return Result.ok(authService.getJwt(jwtToken));
    }

}
