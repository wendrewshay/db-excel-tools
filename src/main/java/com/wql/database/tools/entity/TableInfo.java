package com.wql.database.tools.entity;

import lombok.Data;

/**
 * 表元信息
 * Created by wendrewshay on 2019/05/31
 */
@Data
public class TableInfo {
    /**
     * 表名
     */
    private String tableName;
    /**
     * 表注释
     */
    private String tableComment;
}
