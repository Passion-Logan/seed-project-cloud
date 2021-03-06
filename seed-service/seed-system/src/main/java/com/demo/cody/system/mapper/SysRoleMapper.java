package com.demo.cody.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.cody.model.entity.SysRole;
import com.demo.cody.model.vo.system.request.SysRoleQueryVO;
import com.demo.cody.model.vo.system.response.SysRoleResponseVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 查询用户角色
     *
     * @param userId userId
     * @return SysRole
     */
    List<SysRole> getRolesByUserId(@Param("userId") Long userId);

    /**
     * @param page  page
     * @param query query
     * @return SysRoleResponseVO
     */
    IPage<SysRoleResponseVO> getList(Page<SysRoleResponseVO> page, SysRoleQueryVO query);

}