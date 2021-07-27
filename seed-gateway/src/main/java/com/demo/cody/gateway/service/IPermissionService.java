package com.demo.cody.gateway.service;

public interface IPermissionService {

    /**
     * 网关鉴权
     *
     * @param authentication
     * @param method
     * @param url
     * @return
     */
    boolean permission(String authentication, String url, String method);

}
