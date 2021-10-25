package com.demo.cody.core.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * spring上下文工具类，用于普通类调用springIOC中的对象
 *
 * @author zjh
 * @date 2020/9/8
 * @lastUpdateUser
 * @lastUpdateDesc
 * @lastUpdateTime 2020/9/8
 */
@ConditionalOnClass(name = "javax.servlet.http.HttpServletRequest")
public class SpringUtils extends SpringUtil {

    /**
     * 获取配置文件配置项的值
     *
     * @param key 配置项key
     * @return 属性值
     * @since 5.3.3
     */
    public static String getProperty(String key) {
        return getApplicationContext().getEnvironment().getProperty(key);
    }

    /**
     * 获取HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (ObjectUtil.isNotNull(requestAttributes)) {
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        }
        return null;
    }

    /**
     * 获取HttpServletResponse
     */
    public static HttpServletResponse getResponse() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (ObjectUtil.isNotNull(requestAttributes)) {
            return ((ServletRequestAttributes) requestAttributes).getResponse();
        }
        return null;
    }
}
