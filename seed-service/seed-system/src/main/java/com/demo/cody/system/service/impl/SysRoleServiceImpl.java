package com.demo.cody.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.cody.common.entity.SysRole;
import com.demo.cody.core.util.BeanUtil;
import com.demo.cody.core.vo.system.request.SysRoleQueryVO;
import com.demo.cody.core.vo.system.response.SysRoleResponseVO;
import com.demo.cody.system.mapper.SysRoleMapper;
import com.demo.cody.system.service.ISysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Resource
    private SysRoleMapper roleMapper;

    /**
     * 查询用户角色
     *
     * @param userId userId
     * @return SysRole
     */
    @Override
    public List<SysRole> getRolesByUserId(Long userId) {
        List<SysRole> list = roleMapper.getRolesByUserId(userId);
        return BeanUtil.convert(list, SysRole.class);
    }

    @Override
    public IPage<SysRoleResponseVO> getList(Page<SysRoleResponseVO> page, SysRoleQueryVO query) {
        return roleMapper.getList(page, query);
    }
}
