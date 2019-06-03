package com.wql.database.tools.utils;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * 数据源操作工具类
 * Created by wendrewshay on 2019/05/31
 */
public class DataSourceUtils {

    /**
     * 初始化数据源
     * @param ip        数据库连接IP
     * @param port      数据库连接端口
     * @param username  数据库连接用户名
     * @param password  数据库连接用户名
     * @param dbName    数据库名称
     * @return DataSource
     */
    public static DataSource init(String ip, String port, String username, String password, String dbName) {
        DriverManagerDataSource source = new DriverManagerDataSource();
        source.setDriverClassName("com.mysql.cj.jdbc.Driver");
        source.setUrl(String.format("jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai", ip, port, dbName));
        source.setUsername(username);
        source.setPassword(password);
        return source;
    }
}
