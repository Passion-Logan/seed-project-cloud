package com.demo.cody.system.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.cody.core.aspect.annotation.AutoLog;
import com.demo.cody.core.exception.CustomExecption;
import com.demo.cody.core.utils.response.Result;
import com.demo.cody.model.entity.SysUser;
import com.demo.cody.model.entity.SysUserRole;
import com.demo.cody.model.vo.system.request.SysUserQueryVO;
import com.demo.cody.model.vo.system.request.SysUserRoleRequestVO;
import com.demo.cody.model.vo.system.response.SysUserResponseVO;
import com.demo.cody.system.service.ISysUserRoleService;
import com.demo.cody.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统-用户管理
 *
 * @author wql
 * @date 2021/8/31
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/8/31
 */
@RestController
@Api(value = "SysUserController", tags = "系统-用户管理")
@RequestMapping("/sys/user")
@Validated
@Slf4j
public class SysUserController {

    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ISysUserRoleService sysUserRoleService;

    @ApiOperation(value = "分页查询")
    @PostMapping("getPageList")
    public Result<List<SysUserResponseVO>> selectPageList(@RequestBody @Valid SysUserQueryVO sysUserQueryVO) {
        Page<SysUserResponseVO> page = new Page<>(sysUserQueryVO.getCurrent(), sysUserQueryVO.getPageSize());
        IPage<SysUserResponseVO> data = sysUserService.getList(page, sysUserQueryVO);

        return Result.ok(data.getRecords(), (int) data.getTotal());
    }

    @AutoLog(value = "添加用户", operateType = 2)
    @ApiOperation(value = "添加用户")
    @PostMapping("addUser")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Result<Void> addUser(@RequestBody @Valid SysUserRoleRequestVO user) {
        isUsername(user.getUserName(), null);
        SysUser entity = new SysUser();
        BeanUtils.copyProperties(user, entity);

        String encrypt = new BCryptPasswordEncoder().encode(user.getPassword());
        entity.setPassword(encrypt);
        sysUserService.save(entity);

        Long userId = entity.getId();
        if (StrUtil.isNotEmpty(user.getRoleIds())) {
            updateUserRole(user.getRoleIds(), userId);
        }

        return Result.ok();
    }

    @ApiOperation(value = "编辑用户")
    @PutMapping("updateUser")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Result<Void> editUser(@RequestBody @Valid SysUserRoleRequestVO user) {
        isUsername(user.getUserName(), user.getId());
        SysUser entity = new SysUser();
        BeanUtils.copyProperties(user, entity);

        Long userId = user.getId();
        sysUserRoleService.remove(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, userId));
        updateUserRole(user.getRoleIds(), userId);

        sysUserService.updateById(entity);
        return Result.ok();
    }

    private void updateUserRole(String roleIds, Long userId) {
        if (StrUtil.isNotEmpty(roleIds)) {
            String[] ids = roleIds.split(",");
            List<SysUserRole> userRoles = new ArrayList<>(ids.length);
            for (String id : ids) {
                userRoles.add(SysUserRole.builder().userId(userId).roleId(Long.parseLong(id)).build());
            }
            sysUserRoleService.saveBatch(userRoles);
        }
    }

    @ApiOperation(value = "修改密码")
    @GetMapping("updatePassword")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Result<Void> editPassword(String userName, String password) {
        sysUserService.changePassword(userName, password);
        return Result.ok();
    }

    @AutoLog(value = "删除用户", operateType = 4)
    @ApiOperation(value = "删除用户")
    @DeleteMapping("removeUser")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Result<Void> deleteUser(@RequestBody JSONObject object) {
        List<String> ids = Arrays.asList(object.getString("ids").split(","));
        sysUserService.removeByIds(ids);
        sysUserRoleService.remove(Wrappers.<SysUserRole>lambdaQuery().in(SysUserRole::getUserId, ids));

        return Result.ok();
    }

    @GetMapping("getUserRole")
    @ApiOperation(value = "获取用户角色")
    @DeleteMapping("getUserRole")
    public Result<List<String>> getUserRole(@ApiParam(name = "userId", value = "userId", required = true) @RequestParam(value = "userId") String userId) {
        List<SysUserRole> userRoles = sysUserRoleService.list(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, userId));
        List<String> roleList = userRoles.stream().map(m -> m.getRoleId().toString()).collect(Collectors.toList());
        return Result.ok(roleList);
    }

    @GetMapping("/getByUserName")
    @ApiOperation(value = "根据用户名查找用户")
    public SysUser findByUsername(@RequestParam("userName") String userName) {
        return sysUserService.findByUsername(userName, null);
    }

    private void isUsername(String username, Long id) {
        if (sysUserService.findByUsername(username, id) != null) {
            throw new CustomExecption("账号已存在");
        }
    }

}
