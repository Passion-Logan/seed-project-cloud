package com.demo.cody.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.cody.common.entity.SysMenu;
import com.demo.cody.common.vo.system.response.SysUserMenuResponseVO;
import com.demo.cody.common.vo.system.response.TreeData;

import java.util.List;

public interface ISysMenuService extends IService<SysMenu> {

    /**
     * 获取菜单列表
     *
     * @return
     */
    List<SysUserMenuResponseVO> getList();

    /**
     * 新增菜单
     *
     * @param menu
     * @return
     */
    boolean insertMenu(SysMenu menu);

    /**
     * 编辑菜单
     *
     * @param menu
     * @return
     */
    boolean updateMenu(SysMenu menu);

    /**
     * 批量删除菜单
     *
     * @param ids
     * @return
     */
    boolean deleteBatch(List<String> ids);

    /**
     * 删除单条
     *
     * @param id
     * @return
     */
    boolean deleteById(String id);

    /**
     * 查询用户菜单
     *
     * @param userId
     * @return
     */
    List<SysMenu> findMenuByUserId(String userId);

    /**
     * 查询用户权限
     *
     * @param userId
     * @return
     */
    List<String> getPermissionsByUserId(String userId);


    List<TreeData> queryTreeList();

}
