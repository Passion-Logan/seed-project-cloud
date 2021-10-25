package com.demo.cody.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.cody.common.entity.SysMenu;
import com.demo.cody.core.vo.system.response.SysUserMenuResponseVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 获取菜单列表
     *
     * @return SysUserMenuResponseVO
     */
    List<SysUserMenuResponseVO> getList();

    /**
     * 根据pid批量查询
     *
     * @param pid pid
     * @return SysMenu
     */
    List<SysMenu> getListByPid(@Param("pid") String pid);

    /**
     * 根据用户id查询
     *
     * @param userId userId
     * @return SysMenu
     */
    List<SysMenu> findMenuByUserId(@Param("userId") Long userId);

    /**
     * 查询用户按钮权限
     *
     * @param userId userId
     * @return String
     */
    List<String> getPermissionsByUserId(@Param("userId") Long userId);

    /**
     * 查询权限标识是否重复
     *
     * @param permission permission
     * @param id         id
     * @return int
     */
    int checkPermission(@Param("permission") String permission, @Param("id") Long id);

}
