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
public @interface MenuResources {

    /**
     * 资源扫描
     */
    MenuResource[] resource();

}
