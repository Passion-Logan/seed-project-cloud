package com.demo.cody.common.service.impl;

import com.demo.cody.common.constant.DataBaseConstant;
import com.demo.cody.common.exception.CustomExecption;
import com.demo.cody.common.service.ISysBaseAPI;
import com.demo.cody.common.util.SpringContextUtils;
import com.demo.cody.common.util.oConvertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * @author Administrator
 * @Description: TODO
 * @date: 2020年06月17日 14:13
 */
@Slf4j
@Service
public class SysBaseImpl implements ISysBaseAPI {

    /**
     * 当前系统数据库类型
     */
    private static String DB_TYPE = "";

    @Override
    public String getDatabaseType() throws SQLException {
        if (oConvertUtils.isNotEmpty(DB_TYPE)) {
            return DB_TYPE;
        }
        DataSource dataSource = SpringContextUtils.getApplicationContext().getBean(DataSource.class);
        return getDatabaseTypeByDataSource(dataSource);
    }

    /**
     * 获取数据库类型
     *
     * @param dataSource
     * @return
     * @throws SQLException
     */
    private String getDatabaseTypeByDataSource(DataSource dataSource) throws SQLException {
        if ("".equals(DB_TYPE)) {
            Connection connection = dataSource.getConnection();
            try {
                DatabaseMetaData md = connection.getMetaData();
                String dbType = md.getDatabaseProductName().toLowerCase();
                if (dbType.indexOf("mysql") >= 0) {
                    DB_TYPE = DataBaseConstant.DB_TYPE_MYSQL;
                } else if (dbType.indexOf("oracle") >= 0) {
                    DB_TYPE = DataBaseConstant.DB_TYPE_ORACLE;
                } else if (dbType.indexOf("sqlserver") >= 0 || dbType.indexOf("sql server") >= 0) {
                    DB_TYPE = DataBaseConstant.DB_TYPE_SQLSERVER;
                } else if (dbType.indexOf("postgresql") >= 0) {
                    DB_TYPE = DataBaseConstant.DB_TYPE_POSTGRESQL;
                } else {
                    throw new CustomExecption("数据库类型:[" + dbType + "]不识别!");
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                connection.close();
            }
        }
        return DB_TYPE;

    }
}
