package com.demo.cody.system.filter;

import cn.hutool.core.util.StrUtil;
import com.demo.cody.core.constant.AuthConstant;
import com.demo.cody.core.security.JwtTokenUtils;
import com.demo.cody.model.entity.SysUser;
import com.demo.cody.system.service.ISysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wql
 * @desc AuthInterceptor
 * @date 2021/7/22
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/7/22
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Resource
    private ISysUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Map<String, String> map = new HashMap<>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        String s = map.get(AuthConstant.TOKEN);
        if (StrUtil.isNotBlank(s) && s.startsWith(AuthConstant.JWT_TOKEN_PREFIX)) {
            s = StringUtils.substring(s, AuthConstant.JWT_TOKEN_PREFIX.length());
            SysUser byUsername = userService.findByUsername(JwtTokenUtils.getUsernameFromToken(s), null);
            request.setAttribute("userId", byUsername.getId());
            request.setAttribute("phone", byUsername.getPhone());
        }

        //返回true通过，返回false拦截
        return true;
    }

}
