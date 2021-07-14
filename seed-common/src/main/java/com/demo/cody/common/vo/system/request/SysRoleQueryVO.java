package com.demo.cody.common.vo.system.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import java.io.Serializable;

@ApiModel
@Data
public class SysRoleQueryVO implements Serializable {

    @ApiModelProperty("角色名称")
    private String roleName;

    /**
     * 当前页
     */
    @ApiModelProperty("当前页")
    @Min(value = 1, message = "当前页不小于1")
    private Integer current;
    /**
     * 分页大小
     */
    @ApiModelProperty("分页大小")
    @Min(value = 1, message = "每页条数不能小于1")
    private Integer pageSize;

}
