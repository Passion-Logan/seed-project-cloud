package com.demo.cody.gateway.service.impl;

import com.demo.cody.gateway.feigh.AuthFeignClient;
import com.demo.cody.gateway.service.IPermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 * @author wql
 * @desc PermissionService
 * @date 2021/7/27
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/7/27
 */
@Service
public class PermissionService implements IPermissionService {

    @Resource
    private AuthFeignClient authFeignClient;

    @Override
    public boolean permission(String authentication, String url, String method) {
        return authFeignClient.permission(authentication, url, method);
    }

}
