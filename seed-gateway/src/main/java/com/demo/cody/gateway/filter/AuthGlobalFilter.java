package com.demo.cody.gateway.filter;

import com.demo.cody.gateway.config.IgnoreUrlsConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;

//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        /*String token = exchange.getRequest().getHeaders().getFirst(AuthConstant.JWT_TOKEN_HEADER);
//        String auth = exchange.getRequest().getHeaders().getFirst(AuthConstant.AUTHORITY_CLAIM_NAME);*/
//
//        /*if (StrUtil.isEmpty(token)) {
//            return chain.filter(exchange);
//        }*/
//        //try {
//        //从token中解析用户信息并设置到Header中去
//            /*String realToken = token.replace(AuthConstant.JWT_TOKEN_PREFIX, "");
//            JWSObject jwsObject = JWSObject.parse(realToken);
//            String userStr = jwsObject.getPayload().toString();
//            log.info("AuthGlobalFilter.filter() user:{}",userStr);*/
//        ServerHttpRequest request = exchange.getRequest().mutate()
//                .headers(headers -> {
//                    headers.add("user", "fsdfsdsssssssss");
//                })
//                .build();
//        exchange = exchange.mutate().request(request).build();
//        /*} catch (ParseException e) {
//            e.printStackTrace();
//        }*/
//        return chain.filter(exchange);
//    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String authentication = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
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
        // TODO 调用签权服务看用户是否有权限，若有权限进入下一个filter
        /**
         * if (permissionService.permission(authentication, url, method)) {
         *             ServerHttpRequest.Builder builder = request.mutate();
         *             //TODO 转发的请求都加上服务间认证token
         *             builder.header(X_CLIENT_TOKEN, "TODO zhoutaoo添加服务间简单认证");
         *             //将jwt token中的用户信息传给服务
         *             builder.header(X_CLIENT_TOKEN_USER, getUserToken(authentication));
         *             return chain.filter(exchange.mutate().request(builder.build()).build());
         *         }
         */
        if (true) {
            ServerHttpRequest.Builder builder = request.mutate();
            //将jwt token中的用户信息传给服务
            builder.header(X_CLIENT_TOKEN_USER, authentication);
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
        serverWebExchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        DataBuffer buffer = serverWebExchange.getResponse()
                .bufferFactory().wrap(HttpStatus.UNAUTHORIZED.getReasonPhrase().getBytes());
        return serverWebExchange.getResponse().writeWith(Flux.just(buffer));
    }

}
