package com.demo.cody.system.controller;

import com.demo.cody.model.utils.BasicPageVo;
import com.demo.cody.model.entity.SysLog;
import com.demo.cody.model.entity.SysLoginLog;
import com.demo.cody.model.vo.system.request.SysLogQueryVO;
import com.demo.cody.model.vo.system.request.SysLoginLogQueryVO;
import com.demo.cody.system.service.ISysLogService;
import com.demo.cody.system.service.ISysLoginLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author Administrator
 * @desc SysDataLogController
 * @date 2021/9/14
 * @lastUpdateUser Administrator
 * @lastUpdateDesc
 * @lastUpdateTime 2021/9/14
 */
@RestController
@Api(value = "SysDataLogController", tags = "系统-日志管理")
@RequestMapping("/sys/log")
@Validated
@Slf4j
public class SysDataLogController {

    @Resource
    private ISysLogService sysLogService;
    @Resource
    private ISysLoginLogService loginLogService;

    @ApiOperation(value = "分页查询操作日志")
    @PostMapping("getPageList")
    public BasicPageVo<SysLog> selectPageList(@RequestBody @Valid SysLogQueryVO vo) {
        return sysLogService.getByPage(vo);
    }

    @ApiOperation(value = "分页查询登录日志")
    @PostMapping("getPageLoginList")
    public BasicPageVo<SysLoginLog> selectPageLoginList(@RequestBody @Valid SysLoginLogQueryVO vo) {
        return loginLogService.getByPage(vo);
    }


}
