package com.demo.cody.core.interceptor;

import lombok.extern.slf4j.Slf4j;

/**
 * 用户信息拦截
 *
 * @author wql
 * @desc UserInterceptor
 * @date 2021/7/30
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/7/30
 */
@Slf4j
//public class UserInterceptor implements HandlerInterceptor {
public class UserInterceptor {

    /*@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userInfo = StringUtils.defaultIfBlank(request.getHeader(AuthConstant.X_CLIENT_TOKEN_USER), "{}");
        UserContextHolder.getInstance().setContext(new ObjectMapper().readValue(userInfo, Map.class));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContextHolder.getInstance().clear();
    }*/
}
