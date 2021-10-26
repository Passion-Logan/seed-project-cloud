package com.demo.cody.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.demo.cody.core.aspect.annotation.Stringify;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Description: 部门表
 * @date: 2020年06月17日 15:04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysDept {

    private static final long serialVersionUID = 1L;

    /**
     * 部门id
     */
    @Stringify
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 父部门id
     */
    @Stringify
    private Long pid;
    /**
     * 部门名称
     */
    private String deptName;
    /**
     * 显示顺序
     */
    private Integer sort;
    /**
     * 部门状态:0正常,1停用
     */
    private Boolean enabled;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 备注
     */
    private String remark;
}