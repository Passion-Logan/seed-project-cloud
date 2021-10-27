package com.demo.cody.resource.annotation;

import java.lang.annotation.*;

/**
 * 按钮资源
 *
 * @author wql
 * @date 2021/10/27
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/10/27
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PermissionResource {

    /**
     * 名称
     */
    String name();

    /**
     * 父级标识
     */
    String parentCode() default "";

    /**
     * 排序
     */
    int sort() default 0;

}
