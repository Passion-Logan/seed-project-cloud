package com.demo.cody.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.cody.common.entity.SysMenu;
import com.demo.cody.common.enums.MenulTypeEnum;
import com.demo.cody.common.exception.CustomExecption;
import com.demo.cody.common.vo.system.response.SysUserMenuResponseVO;
import com.demo.cody.common.vo.system.response.TreeData;
import com.demo.cody.system.mapper.SysMenuMapper;
import com.demo.cody.system.service.ISysMenuService;
import com.demo.cody.system.service.ISysRoleMenuService;
import com.demo.cody.system.util.BeanUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: TODO
 * @date: 2020年06月16日 18:45
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    private static final Logger logger = LoggerFactory.getLogger(SysMenuServiceImpl.class);

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private ISysRoleMenuService roleMenuService;

    @Override
    public List<SysUserMenuResponseVO> getList() {
        return sysMenuMapper.getList();
    }

    @Override
    public boolean insertMenu(SysMenu menuDTO) {
        //校验菜单数据
        checkMenuData(menuDTO);
        return this.save(BeanUtil.convert(menuDTO, SysMenu.class));
    }

    /**
     * 校验菜单新增编辑时 参数
     * 由于前端是动态form表单
     * validation注解+后台控制参数校验
     *
     * @param menuDTO
     */
    private void checkMenuData(SysMenu menuDTO) {

        //一级菜单 pid 默认为0
        if (MenulTypeEnum.ROOT_MENU.getValue().equals(menuDTO.getType())) {
            //一级菜单pid=0
            menuDTO.setPid("0");
            if (StringUtils.isBlank(menuDTO.getRedirect())) {
                throw new CustomExecption("跳转路径不能为空");
            }
            if (StringUtils.isBlank(menuDTO.getIcon())) {
                throw new CustomExecption("菜单图标不能为空");
            }
        } else {
            if (menuDTO.getPid() == null) {
                throw new CustomExecption("上级菜单不能为空");
            }
        }

        //类型为菜单时 请求地址不能为空
        if (!MenulTypeEnum.BUTTON.getValue().equals(menuDTO.getType())) {
            if (StringUtils.isBlank(menuDTO.getPath())) {
                throw new CustomExecption("菜单路径不能为空");
            }
            if (StringUtils.isBlank(menuDTO.getComponent())) {
                throw new CustomExecption("前端路由不能为空");
            }
            if (StringUtils.isBlank(menuDTO.getComponentName())) {
                throw new CustomExecption("路由名称不能为空");
            }
        }
        //按钮时 权限标识不能为空
        if (MenulTypeEnum.BUTTON.getValue().equals(menuDTO.getType())) {
            if (StringUtils.isBlank(menuDTO.getPermission())) {
                throw new CustomExecption("权限标识不能为空");
            }
            //判断是否存在相同按钮标识
            int num = sysMenuMapper.checkPermission(menuDTO.getPermission(), menuDTO.getId());
            if (num > 1) {
                logger.error("权限标识重复 permission = " + menuDTO.getPermission());
                throw new CustomExecption("权限标识重复");
            }
        }
    }

    /**
     * 编辑菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean updateMenu(SysMenu menu) {
        //校验菜单数据
        checkMenuData(menu);
        return this.updateMenu(menu);
    }

    /**
     * 批量删除菜单
     *
     * @param ids
     * @return
     */
    @Override
    public boolean deleteBatch(List<String> ids) {
        List<String> idList = new ArrayList<>();
        List<String> names = new ArrayList<>();
        for (String id : ids) {
            //查看是否为子菜单
            List<SysMenu> list = sysMenuMapper.getListByPid(id);
            if (CollectionUtils.isEmpty(list)) {
                //不存在子节点了 则删除
                idList.add(id);
            } else {
                names.add(this.getById(id).getMenu());
            }
        }
        if (names.size() > 0) {
            throw new CustomExecption(StringUtils.join(names, ",") + " 存在子菜单!");
        }

        // 删除角色菜单关联
        for (String id : idList) {
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("menu_id", id);
            roleMenuService.remove(wrapper);
        }

        return this.removeByIds(idList);
    }

    /**
     * 删除单条
     *
     * @param id
     * @return
     */
    @Override
    public boolean deleteById(String id) {
        //查看是否为子菜单
        List<SysMenu> list = sysMenuMapper.getListByPid(id);
        if (!CollectionUtils.isEmpty(list)) {
            throw new CustomExecption("存在子菜单 无法删除");
        }
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("menu_id", id);
        roleMenuService.remove(wrapper);

        return this.removeById(id);
    }

    /**
     * 查询用户菜单
     *
     * @param userId
     * @return
     */
    @Override
    public List<SysMenu> findMenuByUserId(String userId) {
        List<SysMenu> list = sysMenuMapper.findMenuByUserId(userId);
        //由于用户对应多个角色 菜单去重
        List<SysMenu> distinct = list.stream().distinct().collect(Collectors.toList());
        return distinct;
    }

    /**
     * 查询用户权限
     *
     * @param userId
     * @return
     */
    @Override
    public List<String> getPermissionsByUserId(String userId) {
        return sysMenuMapper.getPermissionsByUserId(userId);
    }

    @Override
    public List<TreeData> queryTreeList() {
        List<TreeData> treeList = new ArrayList<>();
        List<SysUserMenuResponseVO> list = menuService.getList();

        if (list.size() > 0) {
            for (SysUserMenuResponseVO vo : list) {
                treeList.add(TreeData.builder()
                        .pid(vo.getPid())
                        .key(vo.getId())
                        .sort(vo.getSort())
                        .title(vo.getMenu())
                        .value(vo.getId())
                        .build());
            }

            return buildMenuTree(treeList);
        }
        return treeList;
    }

    private List<TreeData> buildMenuTree(List<TreeData> list) {
        Map<String, List<TreeData>> map = list.stream()
                .sorted(Comparator.comparing(TreeData::getSort))
                .collect(Collectors.groupingBy(TreeData::getPid));

        List<TreeData> directory = map.get("0");

        directory.forEach(menu -> {
            List<TreeData> children = map.get(menu.getValue());
            if (children != null) {
                menu.setChildren(children);
            } else {
                menu.setChildren(new ArrayList<>(0));
            }
        });

        return directory;
    }

}