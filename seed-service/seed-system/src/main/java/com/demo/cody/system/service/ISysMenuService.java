package com.demo.cody.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.cody.model.entity.SysMenu;
import com.demo.cody.model.vo.system.response.SysUserMenuResponseVO;
import com.demo.cody.model.vo.system.response.TreeData;

import java.util.List;

public interface ISysMenuService extends IService<SysMenu> {

    /**
     * 获取菜单列表
     *
     * @return SysUserMenuResponseVO
     */
    List<SysUserMenuResponseVO> getList();

    /**
     * 新增菜单
     *
     * @param menu menu
     * @return boolean
     */
    boolean insertMenu(SysMenu menu);

    /**
     * 编辑菜单
     *
     * @param menu menu
     * @return boolean
     */
    boolean updateMenu(SysMenu menu);

    /**
     * 批量删除菜单
     *
     * @param ids ids
     * @return boolean
     */
    boolean deleteBatch(List<String> ids);

    /**
     * 删除单条
     *
     * @param id id
     * @return boolean
     */
    boolean deleteById(String id);

    /**
     * 查询用户菜单
     *
     * @param userId userId
     * @return SysMenu
     */
    List<SysMenu> findMenuByUserId(Long userId);

    /**
     * 查询用户权限
     *
     * @param userId userId
     * @return String
     */
    List<String> getPermissionsByUserId(Long userId);


    /**
     * 树结构查询
     *
     * @return TreeData
     */
    List<TreeData> queryTreeList();

}
