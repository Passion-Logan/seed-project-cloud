package com.demo.cody.system.controller;

import com.demo.cody.common.aspect.annotation.NoRepeatSubmit;
import com.demo.cody.common.entity.SysLoginLog;
import com.demo.cody.common.entity.SysMenu;
import com.demo.cody.common.entity.SysRole;
import com.demo.cody.common.entity.SysUser;
import com.demo.cody.common.util.BeanUtil;
import com.demo.cody.common.util.IPUtilsPro;
import com.demo.cody.common.util.SecurityUtils;
import com.demo.cody.common.vo.Result;
import com.demo.cody.common.vo.system.LoginRequestVO;
import com.demo.cody.common.vo.system.response.MenuResponseVO;
import com.demo.cody.common.vo.system.response.SysRoleResponseVO;
import com.demo.cody.common.vo.system.response.SysUserInfoResponseVO;
import com.demo.cody.system.service.ISysLoginLogService;
import com.demo.cody.system.service.ISysMenuService;
import com.demo.cody.system.service.ISysRoleService;
import com.demo.cody.system.service.ISysUserService;
import com.wf.captcha.ArithmeticCaptcha;
import com.zengtengpeng.operation.RedissonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private ISysMenuService sysMenuService;

    @Resource
    private ISysLoginLogService loginLogService;

    @Resource
    private RedissonObject redissonObject;

    /**
     * 登录接口
     * 注意：如果使用此登录控制器触发登录认证，需要禁用登录认证过滤器，即将 WebSecurityConfig 中的以下配置项注释即可，
     * http.addFilterBefore(new JwtLoginFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
     * 否则访问登录接口会被过滤拦截，
     * 执行不会再进入此登录接口，大家根据使用习惯二选一即可
     */
    /*@NoRepeatSubmit
    @PostMapping(value = "/login")
    public JwtAuthenticatioToken login(@RequestBody LoginRequestVO loginRequestVO, HttpServletRequest request) {
        // 查询验证码
        String code = redissonObject.getValue(loginRequestVO.getUuid());
        // 清除验证码
        redissonObject.delete(loginRequestVO.getUuid());
        if (StringUtils.isBlank(code)) {
            logger.error("验证码不存在或已过期");
            throw new CustomExecption("验证码不存在或已过期");
        }
        if (StringUtils.isBlank(loginRequestVO.getImgCode()) || !loginRequestVO.getImgCode().equalsIgnoreCase(code)) {
            logger.error("验证码错误");
            throw new CustomExecption("验证码错误");
        }
        String username = loginRequestVO.getUsername();
        String password = loginRequestVO.getPassword();

        // 系统登录认证
        JwtAuthenticatioToken token = SecurityUtils.login(request, username, password, authenticationManager);

        //添加登录日志
        insertLoginLog(loginRequestVO, request);
        return token;
    }*/

    /**
     * 记录登录日志
     *
     * @param loginRequestVO loginRequestVO
     * @param request        request
     */
    private void insertLoginLog(LoginRequestVO loginRequestVO, HttpServletRequest request) {
        //记录登录日志
        SysLoginLog loginLog = new SysLoginLog();
        loginLog.setLoginName(loginRequestVO.getUsername());
        String ip = IPUtilsPro.getIpAddr(request);
        loginLog.setIp(ip);
        loginLog.setBrowser(IPUtilsPro.getBrowser(request));
        loginLog.setOs(IPUtilsPro.getOperatingSystem(request));
        loginLog.setStatus(0);
        loginLog.setLoginTime(new Date());

        loginLogService.save(loginLog);
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

    /**
     * 登录用户信息
     *
     * @return SysUserInfoResponseVO
     */
    @ApiOperation("登录用户信息")
    @GetMapping(value = "/user/info")
    public SysUserInfoResponseVO getUserInfo() {
        //查询用户信息
        SysUser userDTO = new SysUser();
        userDTO.setUserName(SecurityUtils.getUsername());
        SysUser user = sysUserService.findByUsername(SecurityUtils.getUsername());
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

    /**
     * 登录查询用户菜单
     *
     * @return Map<String, Object>
     */
    @ApiOperation("登录查询用户菜单")
    @GetMapping(value = "/user/nav")
    public Map<String, Object> getUserNav() {
        Map<String, Object> result = new HashMap<>(2);
        List<SysMenu> list = sysUserService.getUserNav(SecurityUtils.getUsername());
        List<MenuResponseVO> menuPid = getByPid(list, "0");

        if (menuPid.size() > 0) {
            for (MenuResponseVO menu : menuPid) {
                menu.setChildren(getByPid(list, menu.getId()));
            }
        }

        result.put("menuData", menuPid);
        return result;
    }

    private List<MenuResponseVO> getByPid(List<SysMenu> list, String pid) {
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
