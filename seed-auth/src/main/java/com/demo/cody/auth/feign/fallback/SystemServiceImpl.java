package com.demo.cody.auth.feign.fallback;

import com.demo.cody.auth.feign.SystemService;
import com.demo.cody.common.entity.SysLoginLog;
import com.demo.cody.common.entity.SysUser;
import com.demo.cody.common.vo.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author wql
 * @desc SystemServiceImpl
 * @date 2021/9/2
 * @lastUpdateUser wql
 * @lastUpdateDesc
 * @lastUpdateTime 2021/9/2
 */
@Slf4j
@AllArgsConstructor
public class SystemServiceImpl implements SystemService {

    /**
     * 错误
     */
    private final Throwable throwable;

    @Override
    public Result<Boolean> insertLog(SysLoginLog data) {
        log.error("feign 调用登录日志记录{},信息:{}", data, throwable.getLocalizedMessage());
        return Result.error("日志记录失败");
    }

    @Override
    public SysUser findByUsername(String userName) {
        log.error("feign 调用用户查询失败,信息:{}", throwable.getLocalizedMessage());
        return null;
    }

    @Override
    public List<String> getPermissionsByUserId(String userId) {
        log.error("feign 调用权限查询失败,信息:{}", throwable.getLocalizedMessage());
        return null;
    }
}
