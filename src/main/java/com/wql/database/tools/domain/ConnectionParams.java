package com.wql.database.tools.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 数据库连接参数
 * Created by wendrewshay on 2019/05/31
 */
@Data
public class ConnectionParams {
    /**
     * 数据库连接IP
     */
    @NotBlank(message = "数据库连接IP不能为空")
    private String ip;
    /**
     * 数据库连接端口
     */
    @NotBlank(message = "数据库连接端口不能为空")
    private String port;
    /**
     * 数据库连接用户名
     */
    @NotBlank(message = "数据库连接用户名不能为空")
    private String username;
    /**
     * 数据库连接密码
     */
    @NotBlank(message = "数据库连接密码不能为空")
    private String password;
    /**
     * 数据库名称
     */
    @NotBlank(message = "数据库名称不能为空")
    private String dbName;
}
