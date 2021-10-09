package com.demo.cody.gateway.filter;

import cn.hutool.core.util.StrUtil;
import com.demo.cody.common.constant.AuthConstant;
import com.demo.cody.gateway.config.IgnoreUrlsConfig;
import com.demo.cody.gateway.feign.AuthFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 将登录用户的JWT转化成用户信息的全局过滤器
 *
 * @author wql
 * @desc AuthGlobalFilter
 * @date 2021/7/22
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/7/22
 */
@Slf4j
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private static final String X_CLIENT_TOKEN_USER = "x-client-token-user";

    @Resource
    private IgnoreUrlsConfig ignoreUrlsConfig;
    @Resource
    private AuthFeignClient authFeignClient;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String authentication = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String token = request.getHeaders().getFirst(AuthConstant.TOKEN);
        String method = request.getMethodValue();
        String url = request.getPath().value();
        log.debug("url:{},method:{},headers:{}", url, method, request.getHeaders());
        // 不需要鉴权的url
        PathMatcher pathMatcher = new AntPathMatcher();
        List<String> ignoreUrls = ignoreUrlsConfig.getUrls();
        for (String ignoreUrl : ignoreUrls) {
            if (pathMatcher.match(ignoreUrl, url)) {
                return chain.filter(exchange);
            }
        }
        if (StrUtil.isBlank(authentication) && StrUtil.isNotBlank(token)) {
            authentication = "Bearer " + token;
        }
        if (authFeignClient.permission(authentication, url, method)) {
            ServerHttpRequest.Builder builder = request.mutate();
            //将jwt token中的用户信息传给服务
            builder.header(AuthConstant.TOKEN, authentication);
            return chain.filter(exchange.mutate().request(builder.build()).build());
        }
        return unauthorized(exchange);
    }


    /**
     * 网关拒绝，返回401
     *
     * @param
     */
    private Mono<Void> unauthorized(ServerWebExchange serverWebExchange) {
        return Mono.defer(() -> Mono.just(serverWebExchange.getResponse()))
                .flatMap(response -> {
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    HttpHeaders headers = response.getHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    String body = "{\"code\":401,\"message\":\"token不合法或过期\"}";
                    DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
                    return response.writeWith(Mono.just(buffer))
                            .doOnError(error -> DataBufferUtils.release(buffer));
                });
    }

}
