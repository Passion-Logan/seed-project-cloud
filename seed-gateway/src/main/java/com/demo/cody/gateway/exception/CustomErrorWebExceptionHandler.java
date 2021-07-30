package com.demo.cody.gateway.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author wql
 * @desc CustomErrorWebExceptionHandler
 * @date 2021/7/30
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/7/30
 */
@Slf4j
public class CustomErrorWebExceptionHandler extends DefaultErrorWebExceptionHandler {

    @Autowired
    private GateWayExceptionHandlerAdvice gateWayExceptionHandlerAdvice;

    public CustomErrorWebExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties, ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    @Override
    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Map<String, Object> error = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
        int errorStatus = getHttpStatus(error);
        Throwable throwable = getError(request);
        return ServerResponse.status(errorStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(gateWayExceptionHandlerAdvice.handle(throwable)));
    }
}
