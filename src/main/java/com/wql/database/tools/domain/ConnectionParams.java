package com.wql.database.tools.domain;

import lombok.Data;

/**
 * 数据库连接参数
 * Created by wendrewshay on 2019/05/31
 */
@Data
public class ConnectionParams {
    /**
     * 数据库连接IP
     */
    private String ip;
    /**
     * 数据库连接端口
     */
    private String port;
    /**
     * 数据库连接用户名
     */
    private String username;
    /**
     * 数据库连接密码
     */
    private String password;
    /**
     * 数据库名称
     */
    private String dbName;
}
