package com.demo.cody.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.cody.common.entity.SysRole;
import com.demo.cody.common.vo.system.request.SysRoleQueryVO;
import com.demo.cody.common.vo.system.response.SysRoleResponseVO;

import java.util.List;

public interface ISysRoleService extends IService<SysRole> {

    /**
     * 查询用户角色
     *
     * @param userId userId
     * @return SysRole
     */
    List<SysRole> getRolesByUserId(Long userId);

    IPage<SysRoleResponseVO> getList(Page<SysRoleResponseVO> page, SysRoleQueryVO query);
}
