package com.demo.cody.system.controller;

import com.demo.cody.common.aspect.annotation.NoRepeatSubmit;
import com.demo.cody.common.entity.SysLoginLog;
import com.demo.cody.common.entity.SysMenu;
import com.demo.cody.common.entity.SysRole;
import com.demo.cody.common.entity.SysUser;
import com.demo.cody.common.security.JwtTokenUtils;
import com.demo.cody.common.util.BeanUtil;
import com.demo.cody.common.vo.Result;
import com.demo.cody.common.vo.system.request.SysUserPwdVO;
import com.demo.cody.common.vo.system.response.MenuResponseVO;
import com.demo.cody.common.vo.system.response.SysRoleResponseVO;
import com.demo.cody.common.vo.system.response.SysUserInfoResponseVO;
import com.demo.cody.common.vo.system.response.UserInfoResponseVO;
import com.demo.cody.system.service.ISysLoginLogService;
import com.demo.cody.system.service.ISysMenuService;
import com.demo.cody.system.service.ISysRoleService;
import com.demo.cody.system.service.ISysUserService;
import com.wf.captcha.ArithmeticCaptcha;
import com.zengtengpeng.operation.RedissonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统-spring security登录
 *
 * @author wql
 * @date 2021/8/31
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/8/31
 */
@Api(value = "LoginController", tags = "系统-spring security登录")
@RequestMapping("/auth")
@RestController
@Slf4j
public class LoginController {

    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ISysRoleService roleService;
    @Resource
    private ISysLoginLogService loginLogService;
    @Resource
    private RedissonObject redissonObject;
    @Resource
    private ISysMenuService sysMenuService;

    /**
     * 记录日志
     *
     * @param data data
     * @return Boolean
     */
    @PostMapping(value = "/insertLog")
    public Result<Boolean> insertLog(@RequestBody SysLoginLog data) {
        log.info("============== 日志信息： {}", data);
        return Result.ok(loginLogService.save(data));
    }

    /**
     * 验证码
     *
     * @return Map<String, Object>
     */
    @NoRepeatSubmit
    @ApiOperation("获取验证码")
    @GetMapping(value = "/captcha")
    public Result<Map<String, Object>> getCode() {
        // 算术类型 https://gitee.com/whvse/EasyCaptcha
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(111, 36);
        // 几位数运算，默认是两位
        captcha.setLen(2);
        // 获取运算的结果
        String result = captcha.text();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        // 保存到redis
        redissonObject.setValue(uuid, result, 60 * 5 * 1000L);
        // 验证码信息
        Map<String, Object> imgResult = new HashMap<String, Object>(2) {{
            put("img", captcha.toBase64());
            put("uuid", uuid);
        }};
        return Result.ok(imgResult);
    }

    @ApiOperation("更新用户信息")
    @PostMapping(value = "/user/updateUser")
    public Result<Void> updateUser(@RequestBody @Validated UserInfoResponseVO vo) {
        SysUser byId = sysUserService.getById(vo.getId());
        BeanUtils.copyProperties(vo, byId, "roles", "id");
        sysUserService.updateById(byId);

        return Result.ok("更新成功");
    }

    @ApiOperation("用户修改密码")
    @PostMapping(value = "/user/updatePwd")
    public Result<Void> updatePwd(@RequestBody @Validated SysUserPwdVO vo, @RequestAttribute Long userId) {
        sysUserService.verifyPassword(vo, userId);
        return Result.ok("修改成功");
    }

    @PostMapping("/user/uploadAvatar")
    public Result<Void> uploadAvatar(HttpServletRequest request) {
        /*String savePath;
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("file");// 获取上传文件对象

        String bizPath = request.getParameter("biz");
        if (oConvertUtils.isEmpty(bizPath)) {
            bizPath = CommonConstant.UPLOAD_TYPE_OSS.equals(uploadType) ? "upload" : "";
        }

        if (CommonConstant.UPLOAD_TYPE_LOCAL.equals(uploadType)) {
            savePath = this.uploadLocal(file, bizPath);
        } else {
            savePath = sysBaseAPI.upload(file, bizPath, uploadType);
        }

        if (StrUtil.isNotBlank(savePath)) {
            return Result.ok(savePath);
        }
        return Result.error("上传失败!");*/
        return Result.ok("/12312/321");
    }

    /**
     * 登录用户信息
     *
     * @return SysUserInfoResponseVO
     */
    @ApiOperation("登录用户信息")
    @GetMapping(value = "/user/info")
    public SysUserInfoResponseVO getUserInfo(@RequestAttribute Long userId) {
        //查询用户信息
        SysUser user = sysUserService.getById(userId);
        SysUserInfoResponseVO responseVo = BeanUtil.convert(user, SysUserInfoResponseVO.class);

        //查询角色信息
        List<SysRole> roles = roleService.getRolesByUserId(user.getId());
        List<SysRoleResponseVO> roleList = BeanUtil.convert(roles, SysRoleResponseVO.class);
        Objects.requireNonNull(responseVo).setRoles(roleList);

        //查询角色权限信息
        List<String> permissions = sysMenuService.getPermissionsByUserId(user.getId());
        responseVo.setPermissions(permissions);

        return responseVo;
    }

    @ApiOperation("获取用户信息")
    @GetMapping(value = "/user/getInfo")
    public Result<UserInfoResponseVO> getInfo(@RequestAttribute Long userId) {
        //查询用户信息
        SysUser user = sysUserService.getById(userId);
        UserInfoResponseVO responseVo = BeanUtil.convert(user, UserInfoResponseVO.class);

        //查询角色信息
        List<SysRole> roles = roleService.getRolesByUserId(user.getId());
        String[] role = roles.stream().map(SysRole::getRoleName).toArray(String[]::new);
        Objects.requireNonNull(responseVo).setRoles(role);

        return Result.ok(responseVo);
    }

    /**
     * 登录查询用户菜单
     *
     * @return Map<String, Object>
     */
    @ApiOperation("登录查询用户菜单")
    @GetMapping(value = "/user/nav")
    public Map<String, Object> getUserNav() {
        Map<String, Object> result = new HashMap<>(2);
        List<SysMenu> list = sysUserService.getUserNav(JwtTokenUtils.getUsernameFromToken());
        List<MenuResponseVO> menuPid = getByPid(list, Long.valueOf("0"));

        if (menuPid.size() > 0) {
            for (MenuResponseVO menu : menuPid) {
                menu.setChildren(getByPid(list, menu.getId()));
            }
        }

        result.put("menuData", menuPid);
        return result;
    }

    private List<MenuResponseVO> getByPid(List<SysMenu> list, Long pid) {
        return list.stream().filter(item -> pid.equals(item.getPid())).map(item -> MenuResponseVO.builder()
                .id(item.getId())
                .name(item.getMenu())
                .path(item.getPath())
                .redirect(item.getRedirect())
                .icon(item.getIcon())
                .target(item.getIsFrame() ? "_blank" : null)
                .hideInMenu(!item.getVisible())
                .sort(item.getSort())
                .isFrame(item.getIsFrame())
                .build()).sorted(Comparator.comparing(MenuResponseVO::getSort)).collect(Collectors.toList());
    }

}
