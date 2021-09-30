package com.demo.cody.common.vo.system.request;

import com.demo.cody.common.aspect.annotation.Stringify;
import lombok.Data;

@Data
public class SysUserRoleRequestVO {

    @Stringify
    private Long id;

    private String userName;

    private String nickName;

    private String password;

    private String email;

    private Integer sex;

    private Boolean enabled;

    private String roleIds;

}
