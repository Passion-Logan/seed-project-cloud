package com.demo.cody.gateway.utils;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;

import java.util.Map;
import java.util.Optional;

import static com.demo.cody.common.constant.AuthConstant.*;
import static com.demo.cody.common.constant.StringConstant.EMPTY;

/**
 * @author wql
 * @desc ServletUtils
 * @date 2021/9/10
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/9/10
 */
public class ServletUtils {

    /**
     * 获取请求参数
     *
     * @param request request
     * @return map map
     */
    public static Map<?, ?> getQueryParamsMap(ServerRequest request) {
        MultiValueMap<String, String> multiValueMap = request.queryParams();
        if (MapUtil.isNotEmpty(multiValueMap)) {
            return multiValueMap.toSingleValueMap();
        }
        return multiValueMap;
    }

    /**
     * 获取请求参数
     *
     * @param request request
     * @return map map
     */
    public static Map<?, ?> getHeaderMap(ServerRequest request) {
        ServerRequest.Headers headers = request.headers();
        if (ObjectUtil.isNull(headers)) {
            HttpHeaders httpHeaders = headers.asHttpHeaders();
            if (ObjectUtil.isNull(httpHeaders)) {
                return httpHeaders.toSingleValueMap();
            }
        }
        return MapUtil.newHashMap();
    }

    /**
     * 获取请求参数
     *
     * @param request request
     * @return map map
     */
    public static Map<?, ?> getQueryParamsMap(ServerHttpRequest request) {
        MultiValueMap<String, String> multiValueMap = request.getQueryParams();
        if (MapUtil.isNotEmpty(multiValueMap)) {
            return multiValueMap.toSingleValueMap();
        }
        return multiValueMap;
    }

    /**
     * 获取请求参数
     *
     * @param request request
     * @return map map
     */
    public static Map<?, ?> getHeaderMap(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        if (ObjectUtil.isNull(headers)) {
            return headers.toSingleValueMap();
        }
        return MapUtil.newHashMap();
    }

    /**
     * 获取请求参数
     *
     * @param request request
     * @return map map
     */
    public static Map<String, String> getQueryParamsStrMap(ServerRequest request) {
        MultiValueMap<String, String> multiValueMap = request.queryParams();
        if (MapUtil.isNotEmpty(multiValueMap)) {
            return multiValueMap.toSingleValueMap();
        }
        return MapUtil.<String, String>builder().map();
    }

    /**
     * 获取请求参数
     *
     * @param serverWebExchange serverWebExchange
     * @return map map
     */
    public static Map<String, String> getQueryParamsMap(ServerWebExchange serverWebExchange) {
        MultiValueMap<String, String> multiValueMap = serverWebExchange.getRequest().getQueryParams();
        if (MapUtil.isNotEmpty(multiValueMap)) {
            return multiValueMap.toSingleValueMap();
        }
        return MapUtil.<String, String>builder().map();
    }

    /**
     * 获取请求map中指定key
     *
     * @param request request
     * @return map map
     */
    public static String get(ServerHttpRequest request, String attrName) {
        return get(request, attrName, String.class);
    }

    /**
     * 获取请求map中指定key
     *
     * @param attrName attrName
     * @return map map
     */
    public static String get(Map<?, ?> dataMap, String attrName) {
        return MapUtil.get(dataMap, attrName, String.class);
    }

    /**
     * 获取请求map中指定key
     *
     * @param attrName attrName
     * @return map map
     */
    public static <T> T get(Map<?, ?> dataMap, String attrName, Class<T> tClass) {
        return MapUtil.get(dataMap, attrName, tClass);
    }

    /**
     * 获取请求map中指定key
     *
     * @param request request
     * @return map map
     */
    public static <T> T get(ServerHttpRequest request, String attrName, Class<T> tClass) {
        return MapUtil.get(getQueryParamsMap(request), attrName, tClass);
    }

    /**
     * 获取请求map中指定key
     *
     * @param request request
     * @return map map
     */
    public static String get(ServerRequest request, String attrName) {
        return get(request, attrName, String.class);
    }

    /**
     * 获取请求map中指定key
     *
     * @param request ServletUtils
     * @return map map
     */
    public static <T> T get(ServerRequest request, String attrName, Class<T> tClass) {
        return MapUtil.get(getQueryParamsMap(request), attrName, tClass);
    }

    /**
     * 获取token
     *
     * @param exchange exchange
     * @return map map
     */
    public static String getToken(ServerWebExchange exchange) {
        return Optional.ofNullable(exchange.getRequest().getHeaders().getFirst(JWT_TOKEN_HEADER))
                .map(str -> str.replaceAll(JWT_TOKEN_PREFIX, EMPTY))
                .orElse(EMPTY);
    }

    /**
     * 获取token
     *
     * @param exchange exchange
     * @return map map
     */
    public static String getClientToken(ServerWebExchange exchange) {
        return Optional.ofNullable(exchange.getRequest().getHeaders().getFirst(USER_TOKEN_HEADER))
                .map(str -> str.replaceAll(JWT_TOKEN_PREFIX, EMPTY))
                .orElse(EMPTY);
    }

    /**
     * post请求获取body
     *
     * @param exchange exchange
     * @return map map
     */
    /*public static Map<?, ?> getBody(ServerWebExchange exchange) {
        Object attribute = exchange.getAttribute(CACHE_REQUEST_BODY_OBJECT_KEY);
        if (ObjectUtil.isNotEmpty(attribute)) {
            JSONObject jsonObject = JSONUtil.parseObj(attribute);
            if (!JSONUtil.isNull(jsonObject)) {
                return jsonObject.toBean(Map.class);
            }
        }
        return MapUtil.builder().map();
    }*/

}
