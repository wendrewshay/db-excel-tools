package com.wql.database.tools.entity;

import lombok.Data;

/**
 * 字段元信息
 * Created by wendrewshay on 2019/06/03
 */
@Data
public class ColumnInfo {
    /**
     * 字段名称
     */
    private String columnName;
    /**
     * 字段类型
     */
    private String columnType;
    /**
     * 字段键
     */
    private String columnKey;
    /**
     * 是否可为空
     */
    private String isNullable;
    /**
     * 字段默认值
     */
    private String columnDefault;
    /**
     * 字段注释
     */
    private String columnComment;
}
