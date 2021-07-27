package com.demo.cody.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.cody.common.entity.SysLog;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统日志表 服务类
 * </p>
 */
public interface ISysLogService extends IService<SysLog> {

    /**
     * @功能：清空所有日志记录
     */
    public void removeAll();

    /**
     * 获取系统总访问次数
     *
     * @return Long
     */
    Long findTotalVisitCount();

    //update-begin--Author:zhangweijian  Date:20190428 for：传入开始时间，结束时间参数

    /**
     * 获取系统今日访问次数
     *
     * @return Long
     */
    Long findTodayVisitCount(Date dayStart, Date dayEnd);

    /**
     * 获取系统今日访问 IP数
     *
     * @return Long
     */
    Long findTodayIp(Date dayStart, Date dayEnd);
    //update-end--Author:zhangweijian  Date:20190428 for：传入开始时间，结束时间参数

}