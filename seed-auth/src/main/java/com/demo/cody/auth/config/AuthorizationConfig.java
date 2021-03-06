package com.demo.cody.auth.config;

import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.demo.cody.auth.properties.SecurityConfigProperties;
import com.demo.cody.auth.service.security.CustomUserService;
import com.demo.cody.core.utils.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.annotation.Resource;

/**
 * ClassName: OAuth2AuthorizationConfig
 *
 * @author WQL
 * @Description: 认证服务器配置
 * @date: 2021/7/11 16:31
 * @since JDK 1.8
 */
@Slf4j
@Configuration
@EnableAuthorizationServer
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    @Resource
    private SecurityConfigProperties properties;
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private CustomUserService userService;
    @Resource
    private RedisConnectionFactory redisConnectionFactory;

    @Value("${spring.security.oauth2.jwt.signingKey}")
    private String signingKey;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(properties.getClientId())
                .secret(passwordEncoder.encode(properties.getClientSecret()))
                .scopes(properties.getScope())
                .authorizedGrantTypes("authorization_code", "password", "refresh_token");
        log.debug("ClientDetailsServiceConfigurer 已完成。");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                // token存储源
                .tokenStore(tokenStore())
                // 身份认证管理器, 主要用于"password"授权模式
                .authenticationManager(authenticationManager)
                .userDetailsService(userService)
                // 自定义token生成方案
                .accessTokenConverter(jwtAccessTokenConverter());
    }

    /**
     * 允许表单登录
     *
     * @param security security
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
                .allowFormAuthenticationForClients()
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    /**
     * 配置token存储源
     *
     * @return TokenStore
     */
    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    /**
     * 配置jwt生成token的转换
     * 使用自定义Sign Key 进行加密
     *
     * @return JwtAccessTokenConverter
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // 设置签名
        converter.setSigningKey(signingKey);
        return converter;
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, e) -> {
            response.setStatus(HttpStatus.HTTP_OK);
            response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Cache-Control", "no-cache");
            response.getWriter().print(JSONUtil.toJsonStr(Result.error(500, "客户端认证失败")));
            response.getWriter().flush();
        };
    }

}
