package com.demo.cody.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.cody.model.entity.SysRoleMenu;
import com.demo.cody.system.mapper.SysRoleMenuMapper;
import com.demo.cody.system.service.ISysRoleMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {

    /**
     * 保存用户角色权限
     *
     * @param roleId        roleId
     * @param permissionIds permissionIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRolePermission(Long roleId, String permissionIds) {
        //先删除角色菜单
        SysRoleMenu roleMenu = new SysRoleMenu();
        roleMenu.setRoleId(roleId);
        this.remove(Wrappers.<SysRoleMenu>lambdaQuery().eq(SysRoleMenu::getRoleId, roleId));

        //批量添加角色菜单
        String[] menuIds = permissionIds.split(",");
        List<SysRoleMenu> list = new ArrayList<>();
        for (String menuId : menuIds) {
            SysRoleMenu sysRoleMenuDO = new SysRoleMenu();
            sysRoleMenuDO.setRoleId(roleId);
            sysRoleMenuDO.setMenuId(Long.parseLong(menuId));
            list.add(sysRoleMenuDO);
        }

        this.saveBatch(list);
    }

    @Override
    public List<SysRoleMenu> getListByRoleId(String roleId) {
        return this.list(Wrappers.<SysRoleMenu>lambdaQuery().eq(SysRoleMenu::getRoleId, roleId));
    }
}
