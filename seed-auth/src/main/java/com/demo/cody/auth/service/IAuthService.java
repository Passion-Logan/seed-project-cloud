package com.demo.cody.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;


/**
 * @author wql
 * @desc IAuthService
 * @date 2021/7/27
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/7/27
 */
public interface IAuthService {

    /**
     * 校验权限
     *
     * @param authentication authentication
     * @param method         method
     * @param url            url
     * @return true/false
     */
    boolean decide(String authentication, String url, String method);

    /**
     * 调用签权服务，判断用户是否有权限
     *
     * @param authentication authentication
     * @param url            url
     * @param method         method
     * @return true/false
     */
    boolean hasPermission(String authentication, String url, String method);

    /**
     * 从认证信息中提取jwt token 对象
     *
     * @param jwtToken jwtToken
     * @return Jws对象
     */
    Jws<Claims> getJwt(String jwtToken);

    /**
     * 是否无效authentication
     *
     * @param authentication authentication
     * @return boolean
     */
    boolean invalidJwtAccessToken(String authentication);

}
