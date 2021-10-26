package com.demo.cody.model.vo.system;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: TreeSelectModel
 * @date: 2020年06月17日 14:26
 */
@Data
public class TreeSelectModel implements Serializable {

    private static final long serialVersionUID = 9016390975325574747L;

    private String key;

    private String title;

    private boolean isLeaf;

    private String icon;

    private String parentId;

    private String value;

    private String code;

}
