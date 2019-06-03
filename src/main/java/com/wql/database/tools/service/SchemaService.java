package com.wql.database.tools.service;

import com.wql.database.tools.domain.ConnectionParams;
import com.wql.database.tools.entity.TableInfo;
import com.wql.database.tools.utils.DataSourceUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

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
}
