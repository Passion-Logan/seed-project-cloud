package com.demo.cody.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wql
 * @desc TestControlle
 * @date 2021/7/21
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/7/21
 */
@RestController
@RequestMapping("test")
public class TestController {

    @GetMapping("user")
    public String user() {
        return "11123";
    }

}
