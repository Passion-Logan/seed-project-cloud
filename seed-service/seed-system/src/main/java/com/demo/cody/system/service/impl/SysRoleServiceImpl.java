package com.demo.cody.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.cody.common.entity.SysRole;
import com.demo.cody.common.vo.system.request.SysRoleQueryVO;
import com.demo.cody.common.vo.system.response.SysRoleResponseVO;
import com.demo.cody.system.mapper.SysRoleMapper;
import com.demo.cody.system.service.ISysRoleService;
import com.demo.cody.system.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private SysRoleMapper roleMapper;

    /**
     * 查询用户角色
     *
     * @param userId
     * @return
     */
    @Override
    public List<SysRole> getRolesByUserId(String userId) {
        List<SysRole> list = roleMapper.getRolesByUserId(userId);
        return BeanUtil.convert(list, SysRole.class);
    }

    @Override
    public IPage<SysRoleResponseVO> getList(Page<SysRoleResponseVO> page, SysRoleQueryVO query) {
        return roleMapper.getList(page, query);
    }
}