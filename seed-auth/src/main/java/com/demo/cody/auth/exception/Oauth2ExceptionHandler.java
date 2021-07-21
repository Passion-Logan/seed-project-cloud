package com.demo.cody.auth.exception;

import com.demo.cody.common.vo.Result;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局处理Oauth2的异常
 *
 * @author wql
 * @desc Oauth2ExceptionHandler
 * @date 2021/7/21
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/7/21
 */
@RestControllerAdvice
public class Oauth2ExceptionHandler {

    @ExceptionHandler(value = OAuth2Exception.class)
    public Result oAuth2Exception(OAuth2Exception e) {
        return Result.error(e.getMessage());
    }

}
