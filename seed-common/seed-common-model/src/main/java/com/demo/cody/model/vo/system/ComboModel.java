package com.demo.cody.model.vo.system;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Description: ComboModel
 * @date: 2020年06月17日 13:43
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ComboModel implements Serializable {

    private String id;
    private String title;
    /**
     * 文档管理 表单table默认选中
     */
    private boolean checked;
    /**
     * 文档管理 表单table 用户账号
     */
    private String username;
    /**
     * 文档管理 表单table 用户邮箱
     */
    private String email;
    /**
     * 文档管理 表单table 角色编码
     */
    private String roleCode;

}
