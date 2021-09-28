package com.demo.cody.auth.controller;

import com.demo.cody.auth.entity.Oauth2TokenDto;
import com.demo.cody.auth.feign.SystemService;
import com.demo.cody.auth.service.IAuthService;
import com.demo.cody.common.entity.SysLoginLog;
import com.demo.cody.common.util.IPUtilsPro;
import com.demo.cody.common.vo.Result;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;

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
    @Resource
    private SystemService systemService;
    @Resource(name = "defaultThreadPoolExecutor")
    private ThreadPoolExecutor poolExecutor;

    /**
     * Oauth2获取token
     *
     * @param principal  principal
     * @param parameters parameters
     * @param request    request
     * @return Oauth2TokenDto
     * @throws HttpRequestMethodNotSupportedException 异常
     */
    @ApiOperation("Oauth2获取token")
    @PostMapping("/token")
    public Result<Oauth2TokenDto> login(Principal principal, @RequestParam Map<String, String> parameters, HttpServletRequest request) throws HttpRequestMethodNotSupportedException {
        // todo: 网关验证码校验
        OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
        Oauth2TokenDto dto = Oauth2TokenDto.builder()
                .token(Objects.requireNonNull(oAuth2AccessToken).getValue())
                .refreshToken(oAuth2AccessToken.getRefreshToken().getValue())
                .expiresIn(oAuth2AccessToken.getExpiresIn())
                .build();

        // feign 添加登陆日志
        saveLogRecord(parameters.get("username"), request);

        return Result.ok(dto);
    }

    /**
     * 权限校验
     *
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
    public Result<Jws<Claims>> getJwt(@RequestParam String jwtToken) {
        return Result.ok(authService.getJwt(jwtToken));
    }

    /**
     * 日志记录
     *
     * @param username username
     * @param request  request
     */
    private void saveLogRecord(String username, HttpServletRequest request) {
        //记录登录日志
        String ip = IPUtilsPro.getIpAddr(request);
        String browser = IPUtilsPro.getBrowser(request);
        String operatingSystem = IPUtilsPro.getOperatingSystem(request);

        poolExecutor.execute(() -> {
            SysLoginLog build = SysLoginLog.builder().loginName(username).ip(ip).browser(browser).os(operatingSystem)
                    .status(0).loginTime(LocalDateTime.now()).build();

            systemService.insertLog(build);
        });
    }

}
