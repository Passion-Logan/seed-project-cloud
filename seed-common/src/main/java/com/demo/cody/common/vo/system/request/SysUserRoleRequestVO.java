package com.demo.cody.common.vo.system.request;

import com.demo.cody.common.entity.SysUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
public class SysUserRoleRequestVO extends SysUser {

    String roleIds;

}
