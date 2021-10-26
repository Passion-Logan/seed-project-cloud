package com.demo.cody.model.vo.system;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Description: DictModel
 * @date: 2020年06月17日 13:38
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DictModel implements Serializable {

    private static final long serialVersionUID = 1L;

    public DictModel() {
    }

    /**
     * 字典value
     */
    private String value;
    /**
     * 字典文本
     */
    private String text;

}
