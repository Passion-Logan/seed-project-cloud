package com.demo.cody.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.cody.model.utils.BasicPageVo;
import com.demo.cody.model.entity.SysLoginLog;
import com.demo.cody.model.vo.system.request.SysLoginLogQueryVO;

/**
 * 系统访问记录
 */
public interface ISysLoginLogService extends IService<SysLoginLog> {

    /**
     * 分页获取操作日志
     *
     * @param vo
     * @return
     */
    BasicPageVo<SysLoginLog> getByPage(SysLoginLogQueryVO vo);

}
