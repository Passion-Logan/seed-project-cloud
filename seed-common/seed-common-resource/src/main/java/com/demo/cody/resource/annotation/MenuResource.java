package com.demo.cody.resource.annotation;

import java.lang.annotation.*;

/**
 * 菜单资源注解
 *
 * @author wql
 * @date 2021/10/27
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/10/27
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MenuResource {

    /**
     * 名称
     */
    String name();

    /**
     * 路径
     */
    String path();

    /**
     * 父级标识
     */
    String parentCode() default "";

    /**
     * 是否自动覆盖
     */
    boolean autoCover() default false;

    /**
     * 前端路由
     */
    String component() default "";

    /**
     * 前端路由名称
     */
    String componentName();

    /**
     * 排序
     */
    int sort() default 0;

    /**
     * 菜单类型 1 目录 2 菜单 3 按钮
     */
    int type() default 1;

    /**
     * 重定向地址
     */
    String redirect() default "";

    /**
     * 是否显示
     */
    boolean visible() default true;

}
