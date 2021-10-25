package com.demo.cody.core.service;

import java.sql.SQLException;

/**
 * 底层共通业务API，提供其他独立模块调用
 *
 * @author Administrator
 */
public interface ISysBaseAPI {

    /**
     * 获取当前数据库类型
     *
     * @return String
     * @throws SQLException SQLException
     */
    String getDatabaseType() throws SQLException;

}
