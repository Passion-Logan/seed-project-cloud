package com.demo.cody.auth.controller;

import com.demo.cody.auth.entity.Oauth2TokenDto;
import com.demo.cody.auth.service.IAuthService;
import com.demo.cody.common.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

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
@Api(tags = "AuthController", description = "认证中心登录认证")
@RestController
@RequestMapping("/oauth")
public class AuthController {

    @Autowired
    private TokenEndpoint tokenEndpoint;
    @Autowired
    private IAuthService authService;

    @ApiOperation("Oauth2获取token")
    @PostMapping("/token")
    public Result login(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
        Oauth2TokenDto dto = Oauth2TokenDto.builder()
                .token(oAuth2AccessToken.getValue())
                .refreshToken(oAuth2AccessToken.getRefreshToken().getValue())
                .expiresIn(oAuth2AccessToken.getExpiresIn())
                .build();
        return Result.ok(dto);
    }

    @GetMapping("/hasPermission")
    public Result decide(@RequestParam String authentication, @RequestParam String url, @RequestParam String method) {
        return Result.ok(authService.hasPermission(authentication, url, method));
    }

    @GetMapping("getJwt")
    public Result getJwt(String jwtToken) {
        return Result.ok(authService.getJwt(jwtToken));
    }

}
