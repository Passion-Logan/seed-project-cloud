package com.demo.cody.mysql.config;

import cn.hutool.core.util.StrUtil;
import com.demo.cody.core.security.JwtTokenUtils;
import com.demo.cody.mysql.utils.oConvertUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Properties;

/**
 * mybatis拦截器，自动注入创建人、创建时间、修改人、修改时间
 *
 * @author wql
 * @desc MybatisInterceptor
 * @date 2021/7/29
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/7/29
 */
@Slf4j
@Component
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class MybatisInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        String sqlId = mappedStatement.getId();
        log.debug("------sqlId------" + sqlId);
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        Object parameter = invocation.getArgs()[1];
        log.debug("------sqlCommandType------" + sqlCommandType);

        if (parameter == null) {
            return invocation.proceed();
        }
        if (SqlCommandType.INSERT == sqlCommandType) {
//            LoginUser sysUser = this.getLoginUser();
            String username = this.getLoginUser();
            Field[] fields = oConvertUtils.getAllFields(parameter);
            for (Field field : fields) {
                log.debug("------field.name------" + field.getName());
                try {
                    if ("createBy".equals(field.getName())) {
                        field.setAccessible(true);
                        Object local_createBy = field.get(parameter);
                        field.setAccessible(false);
                        if (StringUtils.isBlank(ObjectUtils.toString(local_createBy, ""))) {
                            if (StrUtil.isNotBlank(username)) {
                                // 登录人账号
                                field.setAccessible(true);
                                field.set(parameter, username);
                                field.setAccessible(false);
                            }
                        }
                    }
                    // 注入创建时间
                    if ("createTime".equals(field.getName())) {
                        field.setAccessible(true);
                        Object local_createDate = field.get(parameter);
                        field.setAccessible(false);
                        if (StringUtils.isBlank(ObjectUtils.toString(local_createDate, ""))) {
                            field.setAccessible(true);
                            field.set(parameter, LocalDateTime.now());
                            field.setAccessible(false);
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
        if (SqlCommandType.UPDATE == sqlCommandType) {
            String username = this.getLoginUser();
            Field[] fields = null;
            if (parameter instanceof MapperMethod.ParamMap) {
                MapperMethod.ParamMap<?> p = (MapperMethod.ParamMap<?>) parameter;
                if (p.containsKey("et")) {
                    parameter = p.get("et");
                } else {
                    parameter = p.get("param1");
                }
                if (parameter == null) {
                    return invocation.proceed();
                }
                fields = oConvertUtils.getAllFields(parameter);
            } else {
                fields = oConvertUtils.getAllFields(parameter);
            }

            for (Field field : fields) {
                log.debug("------field.name------" + field.getName());
                try {
                    if ("updateBy".equals(field.getName())) {
                        //获取登录用户信息
                        if (StrUtil.isNotBlank(username)) {
                            // 登录账号
                            field.setAccessible(true);
                            field.set(parameter, username);
                            field.setAccessible(false);
                        }
                    }
                    if ("updateTime".equals(field.getName())) {
                        field.setAccessible(true);
                        field.set(parameter, LocalDateTime.now());
                        field.setAccessible(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // TODO Auto-generated method stub
    }

    private String getLoginUser() {
        return JwtTokenUtils.getUsernameFromToken();
    }

}
