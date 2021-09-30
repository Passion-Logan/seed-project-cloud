package com.demo.cody.upload.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author wql
 * @desc 存储桶
 * @date 2020/12/17 10:38
 * @lastUpdateUser
 * @lastUpdateDesc
 * @lastUpdateTime 2020/12/17 10:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BucketInfo {

    private String name;

    private LocalDateTime creationDate;
}
