package com.demo.cody.auth.service.impl;

import com.demo.cody.auth.service.IAuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author wql
 * @desc AuthService
 * @date 2021/7/27
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/7/27
 */
public class AuthServiceImpl implements IAuthService {

    /**
     * Authorization认证开头是"bearer "
     */
    private static final String BEARER = "Bearer ";

    @Value("${spring.security.oauth2.jwt.signingKey}")
    private String signingKey;

    @Override
    public Jws<Claims> getJws(String jwtToken) {
        if (jwtToken.startsWith(BEARER)) {
            jwtToken = StringUtils.substring(jwtToken, BEARER.length());
        }
        return Jwts.parser()
                .setSigningKey(signingKey.getBytes())
                .parseClaimsJws(jwtToken);
    }

}
