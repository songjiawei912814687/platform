package com.assessment.core.util;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DbUtil {

    // 定义一个获取数据库连接的方法
    public Connection getConnection(String dbIp,String dbPort,String dbName,String dbUser,String dbPassword ) throws SQLException {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName(dbIp);
        dataSource.setPort(Integer.parseInt(dbPort));
        dataSource.setDatabaseName(dbName);
        dataSource.setUser(dbUser);
        dataSource.setPassword(dbPassword);
        dataSource.setCharacterEncoding("UTF-8");
        dataSource.setServerTimezone("Asia/Shanghai");
        dataSource.setAutoReconnect(true);
        dataSource.setFailOverReadOnly(false);
        return dataSource.getConnection();
    }

    public  <T> List<T> queryList(String dbIp,String dbPort,String dbName,String dbUser,String dbPassword,String sql, Class<? extends T> type, Object... params)
            throws SQLException {
        List<T> list;
        QueryRunner run = new QueryRunner();
        BeanListHandler<T> rsh = new BeanListHandler(type);
        Connection conn = this.getConnection(dbIp,dbPort,dbName,dbUser,dbPassword);
        try {
            list = run.query(conn,sql,rsh,params);
        } finally {
            DbUtils.close(conn);
        }
        return list;
    }

}
