package com.demo.cody.gateway.exception;

import com.demo.cody.core.constant.SystemErrorType;
import com.demo.cody.core.vo.Result;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.netty.channel.ConnectTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.security.SignatureException;

/**
 * @author wql
 * @desc GateWayExceptionHandlerAdvice
 * @date 2021/7/30
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/7/30
 */
@Slf4j
@Component
public class GateWayExceptionHandlerAdvice {

    @ExceptionHandler(value = {ResponseStatusException.class})
    public Result<String> handle(ResponseStatusException ex) {
        log.error("response status exception:{}", ex.getMessage());
        return Result.error(SystemErrorType.GATEWAY_ERROR.getMesg());
    }

    @ExceptionHandler(value = {ConnectTimeoutException.class})
    public Result<String> handle(ConnectTimeoutException ex) {
        log.error("connect timeout exception:{}", ex.getMessage());
        return Result.error(SystemErrorType.GATEWAY_CONNECT_TIME_OUT.getMesg());
    }

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<String> handle(NotFoundException ex) {
        log.error("not found exception:{}", ex.getMessage());
        return Result.error(SystemErrorType.GATEWAY_NOT_FOUND_SERVICE.getMesg());
    }

    @ExceptionHandler(value = {ExpiredJwtException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<String> handle(ExpiredJwtException ex) {
        log.error("ExpiredJwtException:{}", ex.getMessage());
        return Result.error(SystemErrorType.INVALID_TOKEN.getMesg());
    }

    @ExceptionHandler(value = {SignatureException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<String> handle(SignatureException ex) {
        log.error("SignatureException:{}", ex.getMessage());
        return Result.error(SystemErrorType.INVALID_TOKEN.getMesg());
    }

    @ExceptionHandler(value = {MalformedJwtException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<String> handle(MalformedJwtException ex) {
        log.error("MalformedJwtException:{}", ex.getMessage());
        return Result.error(SystemErrorType.INVALID_TOKEN.getMesg());
    }

    @ExceptionHandler(value = {RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<String> handle(RuntimeException ex) {
        log.error("runtime exception:{}", ex.getMessage());
        return Result.error("系统错误");
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<String> handle(Exception ex) {
        log.error("exception:{}", ex.getMessage());
        return Result.error("系统错误");
    }

    @ExceptionHandler(value = {Throwable.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<String> handle(Throwable throwable) {
        Result<String> result = Result.error("系统错误");
        if (throwable instanceof ResponseStatusException) {
            result = handle((ResponseStatusException) throwable);
        } else if (throwable instanceof ConnectTimeoutException) {
            result = handle((ConnectTimeoutException) throwable);
        } else if (throwable instanceof RuntimeException) {
            result = handle((RuntimeException) throwable);
        } else if (throwable instanceof Exception) {
            result = handle((Exception) throwable);
        }
        return result;
    }

}
