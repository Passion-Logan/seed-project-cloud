package com.demo.cody.auth.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Oauth2获取Token返回信息封装
 *
 * @author wql
 * @desc Oauth2TokenDto
 * @date 2021/7/21
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/7/21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class Oauth2TokenDto {

    @ApiModelProperty("访问令牌")
    private String token;
    @ApiModelProperty("刷令牌")
    private String refreshToken;
    @ApiModelProperty("访问令牌头前缀")
    private String tokenHead;
    @ApiModelProperty("有效时间（秒）")
    private int expiresIn;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("手机号")
    private String phone;

}
