package com.demo.cody.auth.exception;

import com.demo.cody.common.vo.Result;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
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

    /**
     * 密码错误
     *
     * @param e
     * @return
     */
    @ExceptionHandler(InvalidGrantException.class)
    public Result invalidGrantException(InvalidGrantException e) {
        return Result.error(e.getMessage());
    }

    /**
     * 账户异常(过期，锁定，过期)
     *
     * @param e
     * @return
     */
    @ExceptionHandler({InternalAuthenticationServiceException.class})
    public Result internalAuthenticationServiceException(InternalAuthenticationServiceException e) {
        return Result.error(e.getMessage());
    }
}



