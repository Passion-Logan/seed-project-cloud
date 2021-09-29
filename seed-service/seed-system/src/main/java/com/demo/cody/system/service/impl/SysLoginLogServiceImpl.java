package com.demo.cody.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.cody.common.entity.SysLoginLog;
import com.demo.cody.common.vo.BasicPageVo;
import com.demo.cody.common.vo.system.request.SysLoginLogQueryVO;
import com.demo.cody.system.mapper.SysLoginLogMapper;
import com.demo.cody.system.service.ISysLoginLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements ISysLoginLogService {

    @Override
    public BasicPageVo<SysLoginLog> getByPage(SysLoginLogQueryVO vo) {

        return BasicPageVo.ofPages(this.page(new Page<>(vo.getCurrent(), vo.getPageSize()),
                        Wrappers.<SysLoginLog>lambdaQuery()
                                .like(StrUtil.isNotBlank(vo.getLoginName()), SysLoginLog::getLoginName, vo.getLoginName())
                                .between(Objects.nonNull(vo.getLoginTime()), SysLoginLog::getLoginTime,
                                        Objects.nonNull(vo.getLoginTime()) ? vo.getLoginTime()[0] + " 00:00:00" : "",
                                        Objects.nonNull(vo.getLoginTime()) ? vo.getLoginTime()[1] + " 23:59:59" : "")
                                .orderByDesc(SysLoginLog::getLoginTime)
                )
        );

    }
}
