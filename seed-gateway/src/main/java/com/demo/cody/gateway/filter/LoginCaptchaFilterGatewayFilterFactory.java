package com.demo.cody.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.demo.cody.core.vo.Result;
import com.demo.cody.gateway.utils.ServletUtils;
import com.zengtengpeng.operation.RedissonObject;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.URI;
import java.util.Map;

/**
 * 登陆前置校验过滤器
 *
 * @author wql
 * @desc LoginCaptchaFilterGatewayFilterFactory
 * @date 2021/9/10
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/9/10
 */
@Component
@RequiredArgsConstructor
public class LoginCaptchaFilterGatewayFilterFactory<C> extends AbstractGatewayFilterFactory<C> {

    private final static String AUTH_URL = "/oauth/token";

    @Resource
    private RedissonObject redissonObject;

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest serverHttpRequest = exchange.getRequest();
            URI uri = serverHttpRequest.getURI();
            // 不是登录请求，直接向下执行
            if (!StringUtils.containsIgnoreCase(uri.getPath(), AUTH_URL)) {
                return chain.filter(exchange);
            }
            if (HttpMethod.POST.matches(serverHttpRequest.getMethodValue())) {
                Map<String, String> queryParamsMap = ServletUtils.getQueryParamsMap(exchange);
                try {
                    String username = queryParamsMap.get("username");
                    String password = queryParamsMap.get("password");
                    String code = queryParamsMap.get("imgCode");
                    String uuid = queryParamsMap.get("uuid");
                    // 登陆校验
                    loginCheckPre(username, password, code, uuid);
                } catch (Exception e) {
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.OK);
                    response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
                    String msg = JSON.toJSONString(Result.error(e.getMessage()));
                    DataBuffer bodyDataBuffer = response.bufferFactory().wrap(msg.getBytes());
                    return response.writeWith(Mono.just(bodyDataBuffer));
                }
            }
            return chain.filter(exchange);
        };
    }

    /**
     * 登陆前置检查
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     随机数
     */
    private void loginCheckPre(String username, String password, String code, String uuid) {
        // 非空
        if (StringUtils.isBlank(username)) {
            throw new RuntimeException("用户名不能为空");
        }
        if (StringUtils.isBlank(password)) {
            throw new RuntimeException("密码不能为空");
        }
        if (StringUtils.isBlank(code)) {
            throw new RuntimeException("验证码不能为空");
        }
        if (StringUtils.isBlank(uuid)) {
            throw new RuntimeException("验证码不合法");
        }
        // 验证码校验
        String cacheCode = redissonObject.getValue(uuid);
        // 清除验证码
        redissonObject.delete(uuid);

        if (StringUtils.isBlank(cacheCode)) {
            throw new RuntimeException("验证码已过期");
        }
        if (!cacheCode.equalsIgnoreCase(code)) {
            throw new RuntimeException("验证码错误");
        }
    }

    /**
     * 对应yml中的name配置
     *
     * @return String
     */
    @Override
    public String name() {
        return "CaptchaFilter";
    }
}
