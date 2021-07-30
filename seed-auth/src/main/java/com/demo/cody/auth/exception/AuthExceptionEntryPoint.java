package com.demo.cody.auth.exception;

import com.demo.cody.auth.utils.ResultJsonUtil;
import com.demo.cody.common.constant.AuthConstant;
import com.demo.cody.common.constant.SystemErrorType;
import com.demo.cody.common.vo.Result;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wql
 * @desc AuthExceptionEntryPoint
 * @date 2021/7/30
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/7/30
 */
@Component
public class AuthExceptionEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Throwable cause = authException.getCause();

        response.setStatus(HttpStatus.OK.value());
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        try {
            if(cause instanceof InvalidTokenException) {
                response.getWriter().write(ResultJsonUtil.build(
                        0,
                        2001,
                        "token 格式非法或已失效"
                ));
            }else{
                response.getWriter().write(ResultJsonUtil.build(
                        0,
                        2008,
                        "token 缺失"
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
