package com.demo.cody.auth.service;

import com.demo.cody.common.vo.Result;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import javax.servlet.http.HttpServletRequest;


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
     * @param authentication
     * @param method
     * @param url
     * @return
     */
    boolean decide(String authentication, String url, String method);

    /**
     * 调用签权服务，判断用户是否有权限
     *
     * @param authentication
     * @param url
     * @param method
     * @return true/false
     */
    boolean hasPermission(String authentication, String url, String method);

    /**
     * 从认证信息中提取jwt token 对象
     *
     * @param jwtToken
     * @return Jws对象
     */
    Jws<Claims> getJwt(String jwtToken);

    /**
     * 是否无效authentication
     *
     * @param authentication
     * @return
     */
    boolean invalidJwtAccessToken(String authentication);

}
