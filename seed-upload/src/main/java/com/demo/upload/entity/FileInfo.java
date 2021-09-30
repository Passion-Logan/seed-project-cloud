package com.demo.upload.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author wql
 * @desc 文件信息
 * @date 2020/12/17 10:35
 * @lastUpdateUser
 * @lastUpdateDesc
 * @lastUpdateTime 2020/12/17 10:35
 */
@Data
public class FileInfo {

    /**
     * 名称
     */
    private String name;
    /**
     * 最后修改时间
     */
    private LocalDateTime lastModified;
    /**
     * 拥有者
     */
    private String owner;
    /**
     * 文件大小
     */
    private long size;
}
