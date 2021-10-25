package com.demo.cody.core.util;

import cn.hutool.core.util.ObjectUtil;
import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * 断言工具类
 *
 * @desc AssertUtils
 * @date 2020/11/9 10:29
 * @lastUpdateDesc
 * @lastUpdateTime 2020/11/9 10:29
 */
@UtilityClass
public class AssertUtils {

    /**
     * 断言
     *
     * @param expression content
     * @param msg        msg
     * @throws RuntimeException RuntimeException
     */
    public void isTrue(boolean expression, String msg) throws RuntimeException {
        if (!expression) {
            throw new RuntimeException(msg);
        }
    }

    /**
     * 断言
     *
     * @param object content
     * @param msg    type
     * @throws RuntimeException RuntimeException
     */
    public void isNull(Object object, String msg) throws RuntimeException {
        if (object != null) {
            throw new RuntimeException(msg);
        }
    }

    /**
     * 断言
     *
     * @param object content
     * @param msg    msg
     * @throws RuntimeException RuntimeException
     */
    public void notNull(Object object, String msg) throws RuntimeException {
        if (ObjectUtil.isNull(object)) {
            throw new RuntimeException(msg);
        }
    }

    /**
     * 断言
     *
     * @param text content
     * @param type type
     * @throws RuntimeException RuntimeException
     */
    public void hasLength(String text, String type) throws RuntimeException {
        if (!StringUtils.hasLength(text)) {
            throw new RuntimeException(type);
        }
    }

    /**
     * 断言
     *
     * @param text content
     * @param type type
     * @throws RuntimeException RuntimeException
     */
    public void hasText(String text, String type) throws RuntimeException {
        if (!StringUtils.hasText(text)) {
            throw new RuntimeException(type);
        }
    }

    /**
     * 断言
     *
     * @param array content
     * @param type  type
     * @throws RuntimeException RuntimeException
     */
    public void notEmpty(Object[] array, String type) throws RuntimeException {
        if (ObjectUtils.isEmpty(array)) {
            throw new RuntimeException(type);
        }
    }

    /**
     * 断言
     *
     * @param array content
     * @param type  type
     * @throws RuntimeException RuntimeException
     */
    public void noNullElements(Object[] array, String type) throws RuntimeException {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throw new RuntimeException(type);
                }
            }
        }

    }

    /**
     * 断言
     *
     * @param collection content
     * @param type       type
     * @throws RuntimeException RuntimeException
     */
    public void notEmpty(Collection<?> collection, String type) throws RuntimeException {
        if (CollectionUtils.isEmpty(collection)) {
            throw new RuntimeException(type);
        }
    }

    /**
     * 断言
     *
     * @param map  content
     * @param type type
     * @throws RuntimeException RuntimeException
     */
    public void notEmpty(Map<?, ?> map, String type) throws RuntimeException {
        if (CollectionUtils.isEmpty(map)) {
            throw new RuntimeException(type);
        }
    }

}
