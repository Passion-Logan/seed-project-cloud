package com.demo.cody.core.security;

import com.demo.cody.core.utils.SpringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description: JWT 工具类
 * @date: 2020年06月17日 10:15
 */
public class JwtTokenUtils implements Serializable {

    public static Logger logger = LoggerFactory.getLogger(JwtTokenUtils.class);

    private static final long serialVersionUID = 1L;

    /**
     * 权限列表
     */
    private static final String AUTHORITIES = "authorities";

    /**
     * 密钥
     */
    private static final String SECRET = "cody";

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public static String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims == null) {
            logger.error("从令牌中获取用户名失败");
            throw new RuntimeException("无效的token");
        }
        return claims.get("user_name", String.class);
    }

    public static String getUsernameFromToken() {
        String token = getToken();
        Claims claims = getClaimsFromToken(token);
        if (claims == null) {
            logger.error("从令牌中获取用户名失败");
            throw new RuntimeException("无效的token");
        }
        return claims.get("user_name", String.class);
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private static Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            logger.error("从令牌中获取数据声明 " + e);
            claims = null;
        }
        return claims;
    }

    /**
     * 验证令牌
     *
     * @param token    token
     * @param username username
     * @return Boolean
     */
    public static Boolean validateToken(String token, String username) {
        String userName = getUsernameFromToken(token);
        return (userName.equals(username) && !isTokenExpired(token));
    }

    /**
     * 判断令牌是否过期
     *
     * @param token 令牌
     * @return 是否过期
     */
    public static Boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            logger.error("isTokenExpired " + e);
            return false;
        }
    }

    /**
     * 获取请求token
     *
     * @return token
     */
    public static String getToken() {
        HttpServletRequest request = SpringUtils.getRequest();
        assert request != null;
        String token = request.getHeader("Authorization");
        String tokenHead = "Bearer ";
        if (token == null) {
            token = request.getHeader("token");
        } else if (token.contains(tokenHead)) {
            token = token.substring(tokenHead.length());
        }
        if ("".equals(token)) {
            token = null;
        }
        return token;
    }

}
