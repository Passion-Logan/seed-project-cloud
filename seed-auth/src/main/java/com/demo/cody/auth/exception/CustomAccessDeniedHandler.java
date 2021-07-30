package com.demo.cody.auth.exception;

import com.demo.cody.auth.utils.ResultJsonUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wql
 * @desc CustomAccessDeniedHandler
 * @date 2021/7/30
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/7/30
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException, ServletException {
        response.setStatus(HttpStatus.OK.value());
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        try {
            response.getWriter().write(ResultJsonUtil.build(
                    0,
                    2009,
                    "token 权限不足"
            ));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
