package com.demo.cody.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.cody.core.service.ISysBaseAPI;
import com.demo.cody.model.utils.BasicPageVo;
import com.demo.cody.model.entity.SysLog;
import com.demo.cody.model.entity.basic.SysBaseModel;
import com.demo.cody.model.vo.system.request.SysLogQueryVO;
import com.demo.cody.system.mapper.SysLogMapper;
import com.demo.cody.system.service.ISysLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

@Service
@Slf4j
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

    @Resource
    private SysLogMapper sysLogMapper;
    @Resource
    private ISysBaseAPI sysBaseApi;

    /**
     * 清空所有日志记录
     */
    @Override
    public void removeAll() {
        sysLogMapper.removeAll();
    }

    @Override
    public Long findTotalVisitCount() {
        return sysLogMapper.findTotalVisitCount();
    }

    @Override
    public Long findTodayVisitCount(Date dayStart, Date dayEnd) {
        return sysLogMapper.findTodayVisitCount(dayStart, dayEnd);
    }

    @Override
    public Long findTodayIp(Date dayStart, Date dayEnd) {
        return sysLogMapper.findTodayIp(dayStart, dayEnd);
    }

    @Override
    public BasicPageVo<SysLog> getByPage(SysLogQueryVO vo) {
        Page<SysLog> page = new Page<>(vo.getCurrent(), vo.getPageSize());
        System.out.println(Objects.nonNull(vo.getCreateTime()));
        IPage<SysLog> data = this.page(page, Wrappers.<SysLog>lambdaQuery()
                .like(StrUtil.isNotBlank(vo.getLogContent()), SysLog::getLogContent, vo.getLogContent())
                .like(StrUtil.isNotBlank(vo.getUsername()), SysLog::getUsername, vo.getUsername())
                .eq(StrUtil.isNotBlank(vo.getOperateType()), SysLog::getOperateType, vo.getOperateType())
                .between(Objects.nonNull(vo.getCreateTime()),
                        SysLog::getCreateTime, Objects.nonNull(vo.getCreateTime()) ? vo.getCreateTime()[0] + " 00:00:00" : "",
                        Objects.nonNull(vo.getCreateTime()) ? vo.getCreateTime()[1] + " 23:59:59" : "")
                .orderByDesc(SysBaseModel::getCreateTime)
        );
        return BasicPageVo.ofPages(data);
    }

}
