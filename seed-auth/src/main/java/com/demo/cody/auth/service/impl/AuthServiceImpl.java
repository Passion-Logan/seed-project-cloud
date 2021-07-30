package com.demo.cody.auth.service.impl;

import com.demo.cody.auth.service.IAuthService;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthServiceImpl implements IAuthService {

    /**
     * Authorization认证开头是"Bearer "
     */
    private static final String BEARER = "Bearer ";

    @Value("${spring.security.oauth2.jwt.signingKey}")
    private String signingKey;

    @Override
    public boolean decide(String authentication, String url, String method) {
        log.info("正在访问的url是:{}，method:{}", url, method);
        // TODO 待完善
        //获取用户认证信息
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //获取此url，method访问对应的权限资源信息
        //ConfigAttribute urlConfigAttribute = resourceService.findConfigAttributesByUrl(authRequest);
        //if (NONEXISTENT_URL.equals(urlConfigAttribute.getAttribute()))
        //    log.debug("url未在资源池中找到，拒绝访问");
        //获取此访问用户所有角色拥有的权限资源
        //Set<Resource> userResources = findResourcesByUsername(authentication.getName());
        //用户拥有权限资源 与 url要求的资源进行对比
        //return isMatch(urlConfigAttribute, userResources);
        return true;
    }

    @Override
    public boolean hasPermission(String authentication, String url, String method) {
        // 如果请求未携带token信息, 直接权限
        if (StringUtils.isBlank(authentication) || !authentication.startsWith(BEARER)) {
            log.error("user token is null");
            return Boolean.FALSE;
        }
        //token是否有效，在网关进行校验，无效/过期等
        if (invalidJwtAccessToken(authentication)) {
            return Boolean.FALSE;
        }
        return decide(authentication, url, method);
    }

    @Override
    public Jws<Claims> getJwt(String jwtToken) {
        if (jwtToken.startsWith(BEARER)) {
            jwtToken = StringUtils.substring(jwtToken, BEARER.length());
        }
        jwtToken = StringUtils.substring(jwtToken, BEARER.length());
        return Jwts.parser()
                .setSigningKey(signingKey.getBytes())
                .parseClaimsJws(jwtToken);
    }

    @Override
    public boolean invalidJwtAccessToken(String authentication) {
        // 是否无效true表示无效
        boolean invalid = Boolean.TRUE;
        try {
            getJwt(authentication);
            invalid = Boolean.FALSE;
        } catch (SignatureException | ExpiredJwtException | MalformedJwtException ex) {
            log.error("user token error :{}", ex.getMessage());
        }
        return invalid;
    }

    /**
     * url对应资源与用户拥有资源进行匹配
     *
     * @param urlConfigAttribute
     * @param userResources
     * @return
     */
    /*public boolean isMatch(ConfigAttribute urlConfigAttribute, Set<Resource> userResources) {
        return userResources.stream().anyMatch(resource -> resource.getCode().equals(urlConfigAttribute.getAttribute()));
    }*/

    /**
     * 根据用户所被授予的角色，查询到用户所拥有的资源
     *
     * @param username
     * @return
     */
    /*private Set<Resource> findResourcesByUsername(String username) {
        //用户被授予的角色资源
        Set<Resource> resources = resourceService.queryByUsername(username);
        if (log.isDebugEnabled()) {
            log.debug("用户被授予角色的资源数量是:{}, 资源集合信息为:{}", resources.size(), resources);
        }
        return resources;
    }*/

}
