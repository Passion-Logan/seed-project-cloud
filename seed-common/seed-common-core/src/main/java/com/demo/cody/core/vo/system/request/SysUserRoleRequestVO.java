package com.demo.cody.core.vo.system.request;

import com.demo.cody.core.aspect.annotation.Stringify;
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
