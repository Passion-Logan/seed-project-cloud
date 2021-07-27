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


    Jws<Claims> getJws(String jwtToken);

}
