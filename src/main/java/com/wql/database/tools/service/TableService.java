package com.wql.database.tools.service;

import com.wql.database.tools.domain.ConnectionParams;
import com.wql.database.tools.entity.ColumnInfo;
import com.wql.database.tools.utils.DataSourceUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据库表字段逻辑层
 * Created by wendrewshay on 2019/06/03
 */
@Service
public class TableService {

    /**
     * 查询表字段的sql语句
     */
    private static final String QUERY_COLUMN_INFO_SQL =
            "select COLUMN_NAME as 'columnName', COLUMN_TYPE as 'columnType', COLUMN_KEY as 'columnKey', " +
                    "IS_NULLABLE as 'isNullable', COLUMN_DEFAULT as 'columnDefault', COLUMN_COMMENT as 'columnComment' " +
                    "from information_schema.columns where `table_name`=? and table_schema=?";

    /**
     * 查询表字段信息
     * @author created by wendrewshay on 2019/06/03 10:00
     * @return List<TableInfo>
     */
    public List<ColumnInfo> queryColumnInfo(ConnectionParams params, String tableName) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(DataSourceUtils.init(params.getIp(), params.getPort(), params.getUsername(), params.getPassword(), params.getDbName()));
        List<ColumnInfo> columnInfoList = jdbcTemplate.query(QUERY_COLUMN_INFO_SQL, new String[]{tableName, params.getDbName()}, new BeanPropertyRowMapper<>(ColumnInfo.class));
        return columnInfoList;
    }
}
