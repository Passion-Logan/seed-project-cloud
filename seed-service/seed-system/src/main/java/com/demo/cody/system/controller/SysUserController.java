package com.demo.cody.system.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.cody.common.entity.SysUser;
import com.demo.cody.common.entity.SysUserRole;
import com.demo.cody.common.exception.CustomExecption;
import com.demo.cody.common.vo.Result;
import com.demo.cody.common.vo.system.request.SysUserQueryVO;
import com.demo.cody.common.vo.system.request.SysUserRoleRequestVO;
import com.demo.cody.common.vo.system.response.SysUserResponseVO;
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

    @ApiOperation(value = "添加用户")
    @PostMapping("addUser")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Result<String> addUser(@RequestBody @Valid SysUserRoleRequestVO user) {
        isUsername(user.getUserName());
        SysUser entity = new SysUser();
        BeanUtils.copyProperties(user, entity);

        String encrypt = new BCryptPasswordEncoder().encode(user.getPassword());
        entity.setPassword(encrypt);
        sysUserService.save(entity);

        String userId = entity.getId();
        if (StrUtil.isNotBlank(user.getRoleIds())) {
            String[] ids = splitIds(user.getRoleIds());
            List<SysUserRole> userRoles = new ArrayList<>(ids.length);
            for (String id : ids) {
                userRoles.add(SysUserRole.builder().userId(userId).roleId(id).build());
            }
            sysUserRoleService.saveBatch(userRoles);
        }

        return Result.ok();
    }

    @ApiOperation(value = "编辑用户")
    @PutMapping("updateUser")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Result<String> updateUser(@RequestBody @Valid SysUserRoleRequestVO user) {
        isUsername(user.getUserName());
        SysUser entity = new SysUser();
        BeanUtils.copyProperties(user, entity);

        if (StrUtil.isNotEmpty(user.getRoleIds())) {
            String userId = user.getId();
            sysUserRoleService.remove(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, user.getId()));
            String[] ids = user.getRoleIds().split(",");
            List<SysUserRole> userRoles = new ArrayList<>(ids.length);
            for (String id : ids) {
                userRoles.add(SysUserRole.builder().userId(userId).roleId(id).build());
            }
            sysUserRoleService.saveBatch(userRoles);
        }
        sysUserService.updateById(entity);
        return Result.ok();
    }

    @ApiOperation(value = "修改密码")
    @GetMapping("updatePassword")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Result<String> updatePassword(String userName, String password) {
        sysUserService.changePassword(userName, password);
        return Result.ok();
    }

    @ApiOperation(value = "删除用户")
    @DeleteMapping("removeUser")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Result<String> removeUser(@RequestBody JSONObject object) {
        List<String> ids = Arrays.asList(object.getString("ids").split(","));
        sysUserService.removeByIds(ids);
        sysUserRoleService.remove(Wrappers.<SysUserRole>lambdaQuery().in(SysUserRole::getUserId, ids));

        return Result.ok();
    }

    @GetMapping("getUserRole")
    @ApiOperation(value = "获取用户角色")
    @DeleteMapping("getUserRole")
    public Result<String[]> getUserRole(@ApiParam(name = "userId", value = "userId", required = true) @RequestParam(value = "userId") String userId) {
        List<SysUserRole> userRoles = sysUserRoleService.list(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, userId));
        List<String> roleList = userRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
        String[] roleIds = new String[roleList.size()];

        if (roleList.size() > 0) {
            roleIds = roleList.toArray(new String[0]);
        }
        return Result.ok(roleIds);
    }

    private void isUsername(String username) {
        if (sysUserService.findByUsername(username) != null) {
            throw new CustomExecption("账号已存在");
        }
    }

    private String[] splitIds(String ids) {
        return ids.split(",");
    }

}
