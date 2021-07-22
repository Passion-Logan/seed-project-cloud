package com.demo.cody.system.controller;

import com.demo.cody.common.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wql
 * @desc TestController
 * @date 2021/7/22
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/7/22
 */
@RestController
@RequestMapping("system")
public class TestController {

    @GetMapping("test")
    public Result test() {
        return Result.ok("可以了");
    }

}
