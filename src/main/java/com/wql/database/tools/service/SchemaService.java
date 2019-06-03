package com.wql.database.tools.service;

import com.wql.database.tools.domain.ConnectionParams;
import com.wql.database.tools.entity.ColumnInfo;
import com.wql.database.tools.entity.TableInfo;
import com.wql.database.tools.utils.DataSourceUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Iterator;
import java.util.List;

/**
 * 数据库模式逻辑层
 * Created by wendrewshay on 2019/05/31
 */
@Service
public class SchemaService {
    /**
     * 查询所有表的sql语句
     */
    private static final String QUERY_ALL_TABLES_SQL =
            "select `table_name`, `table_comment` from information_schema.tables where table_schema=?";

    /**
     * 查询表字段的sql语句
     */
    private static final String QUERY_COLUMN_INFO_SQL =
            "select COLUMN_NAME as 'columnName', COLUMN_TYPE as 'columnType', COLUMN_KEY as 'columnKey', " +
                    "IS_NULLABLE as 'isNullable', COLUMN_DEFAULT as 'columnDefault', COLUMN_COMMENT as 'columnComment' " +
                    "from information_schema.columns where `table_name`=? and table_schema=?";

    /**
     * 查询库中所有表
     * @param params 数据库连接参数
     * @author created by wendrewshay on 2019/06/03 10:00
     * @return List<TableInfo>
     */
    public List queryTables(ConnectionParams params) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(DataSourceUtils.init(params.getIp(), params.getPort(), params.getUsername(), params.getPassword(), params.getDbName()));
        List list = jdbcTemplate.query(QUERY_ALL_TABLES_SQL, new String[]{params.getDbName()}, new BeanPropertyRowMapper(TableInfo.class));
        return list;
    }

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

    /**
     * 处理数据并输出表字段信息
     * @param params 连接参数
     */
    public void handle(ConnectionParams params) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(DataSourceUtils.init(params.getIp(), params.getPort(), params.getUsername(), params.getPassword(), params.getDbName()));
        List<TableInfo> tableInfoList = jdbcTemplate.query(QUERY_ALL_TABLES_SQL, new String[]{params.getDbName()}, new BeanPropertyRowMapper(TableInfo.class));

        if (!CollectionUtils.isEmpty(tableInfoList)) {
            Iterator<TableInfo> iterator = tableInfoList.iterator();
            while(iterator.hasNext()) {
                TableInfo tableInfo = iterator.next();
                List<ColumnInfo> columnInfoList = jdbcTemplate.query(QUERY_COLUMN_INFO_SQL, new String[]{tableInfo.getTableName(), params.getDbName()}, new BeanPropertyRowMapper<>(ColumnInfo.class));
                // TODO 处理Excel输出
            }
        }

    }
}
