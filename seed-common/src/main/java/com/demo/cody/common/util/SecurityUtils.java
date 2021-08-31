package com.demo.cody.common.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @Description: Security相关操作
 * @date: 2020年06月17日 9:59
 */
public class SecurityUtils {

    /**
     * 获取令牌进行认证
     */
    /*public static void checkAuthentication(HttpServletRequest request) {
        // 获取令牌并根据令牌获取的登录信息
        Authentication authentication = JwtTokenUtils.getAuthenticationeFromToken(request);
        // 设置登陆信息到上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }*/

    /**
     * 获取用户名
     *
     * @return String
     */
    public static String getUsername() {
        String username = null;
        Authentication authentication = getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            // && principal instanceof UserDetails 暂时去掉  认证principal 为用户名
            if (principal != null) {
                username = principal.toString();
            }
        }
        return username;
    }

    /**
     * 获取登录用户用户名
     *
     * @param authentication authentication
     * @return String
     */
    public static String getUsername(Authentication authentication) {
        String username = null;
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            }
        }

        return username;
    }

    /**
     * 获取当前登录信息
     *
     * @return Authentication
     */
    public static Authentication getAuthentication() {
        if (SecurityContextHolder.getContext() == null) {
            return null;
        }
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
