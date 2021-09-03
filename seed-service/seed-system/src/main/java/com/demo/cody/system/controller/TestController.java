package com.demo.cody.system.controller;

import com.demo.cody.common.vo.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author wql
 * @desc TestController
 * @date 2021/7/22
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/7/22
 */
@Controller
@RequestMapping("test")
public class TestController {

    @GetMapping("user")
    public Result test() {
        return Result.ok("可以了");
    }

}
