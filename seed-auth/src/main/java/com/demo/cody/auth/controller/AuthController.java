package com.demo.cody.auth.controller;

import com.demo.cody.auth.entity.Oauth2TokenDto;
import com.demo.cody.auth.service.IAuthService;
import com.demo.cody.common.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.Map;
import java.util.Objects;

/**
 * Oauth认证
 *
 * @author wql
 * @desc AuthController
 * @date 2021/7/21
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/7/21
 */
@Slf4j
@Api(tags = "AuthController")
@RestController
@RequestMapping("/oauth")
public class AuthController {

    @Resource
    private TokenEndpoint tokenEndpoint;
    @Resource
    private IAuthService authService;

    @ApiOperation("Oauth2获取token")
    @PostMapping("/token")
    @SuppressWarnings("all")
    public Result login(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
        Oauth2TokenDto dto = Oauth2TokenDto.builder()
                .token(Objects.requireNonNull(oAuth2AccessToken).getValue())
                .refreshToken(oAuth2AccessToken.getRefreshToken().getValue())
                .expiresIn(oAuth2AccessToken.getExpiresIn())
                .build();
        return Result.ok(dto);
    }

    /**
     * @param authentication authentication
     * @param url            url
     * @param method         method
     * @return Boolean
     */
    @GetMapping("/hasPermission")
    public Boolean decide(@RequestParam String authentication, @RequestParam String url, @RequestParam String method) {
        return authService.hasPermission(authentication, url, method);
    }

    /**
     * 解析token信息
     *
     * @param jwtToken jwtToken
     * @return Result
     */
    @GetMapping("getJwt")
    @SuppressWarnings("all")
    public Result getJwt(@RequestParam String jwtToken) {
        return Result.ok(authService.getJwt(jwtToken));
    }

}
