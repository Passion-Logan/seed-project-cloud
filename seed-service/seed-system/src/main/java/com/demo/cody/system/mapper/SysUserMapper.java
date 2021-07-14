package com.demo.cody.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.cody.common.entity.SysUser;
import com.demo.cody.common.vo.system.request.SysUserQueryVO;
import com.demo.cody.common.vo.system.response.SysUserResponseVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 更新用户部门id
     *
     * @param deptId
     * @param list
     * @return
     */
    int updateDeptIdByUserIds(@Param("deptId") String deptId,
                              @Param("list") List<String> list);


    /**
     * 解冻 冻结
     *
     * @param status
     * @param list
     * @return
     */
    int frozenBatch(@Param("status") boolean status,
                    @Param("list") List<String> list);

    /**
     * 查询用户信息
     *
     * @param userName
     * @return
     */
    SysUser findByName(@Param("userName") String userName);

    IPage<SysUserResponseVO> getList(Page<SysUserResponseVO> page, SysUserQueryVO sysUserQueryVO);

}
