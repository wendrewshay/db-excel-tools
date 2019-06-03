package com.wql.database.tools.service;

import com.wql.database.tools.domain.ConnectionParams;
import com.wql.database.tools.entity.ColumnInfo;
import com.wql.database.tools.entity.TableInfo;
import com.wql.database.tools.utils.DataSourceUtils;
import com.wql.database.tools.utils.FileUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.sl.usermodel.ColorStyle;
import org.apache.poi.ss.usermodel.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
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
     * 表头
     */
    private static final String[] heads = {"字段名", "字段类型", "键", "是否可为空", "默认值", "备注"};

    /**
     * 查询库中所有表
     * @param params 数据库连接参数
     * @author created by wendrewshay on 2019/06/03 10:00
     * @return List<TableInfo>
     */
    public List queryTables(ConnectionParams params) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate(params);
        List list = jdbcTemplate.query(QUERY_ALL_TABLES_SQL, new String[]{params.getDbName()}, new BeanPropertyRowMapper(TableInfo.class));
        return list;
    }

    /**
     * 查询表字段信息
     * @author created by wendrewshay on 2019/06/03 10:00
     * @return List<TableInfo>
     */
    public List<ColumnInfo> queryColumnInfo(ConnectionParams params, String tableName) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate(params);
        List<ColumnInfo> columnInfoList = jdbcTemplate.query(QUERY_COLUMN_INFO_SQL, new String[]{tableName, params.getDbName()}, new BeanPropertyRowMapper<>(ColumnInfo.class));
        return columnInfoList;
    }

    /**
     * 处理数据并输出表字段信息
     * @param response 响应对象
     * @param params   连接参数
     */
    public void handle(HttpServletResponse response, ConnectionParams params) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate(params);
        List<TableInfo> tableInfoList = jdbcTemplate.query(QUERY_ALL_TABLES_SQL, new String[]{params.getDbName()}, new BeanPropertyRowMapper(TableInfo.class));
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 表头部单元字体
        HSSFFont headFont = workbook.createFont();
        headFont.setFontName("宋体");
        headFont.setBold(true);
        headFont.setFontHeightInPoints((short)11);
        // 表头部单元样式
        HSSFCellStyle headCellStyle = workbook.createCellStyle();
        headCellStyle.setFont(headFont);
        headCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headCellStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
        uniBorderStyleWithThin(headCellStyle);
        // 表内容单元样式1
        HSSFCellStyle aCellStyle = workbook.createCellStyle();
        aCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        uniBorderStyleWithThin(aCellStyle);
        // 表内容单元样式2
        HSSFCellStyle bCellStyle = workbook.createCellStyle();
        bCellStyle.setAlignment(HorizontalAlignment.CENTER);
        bCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        uniBorderStyleWithThin(bCellStyle);
        if (!CollectionUtils.isEmpty(tableInfoList)) {
            for (int m = 0; m < tableInfoList.size(); m ++) {
                TableInfo tableInfo = tableInfoList.get(m);
                String sheetName = !StringUtils.isEmpty(tableInfo.getTableComment()) ? tableInfo.getTableComment() : "未知表" + (m + 1);
                HSSFSheet sheet = workbook.createSheet(sheetName);
                // 设置表头部单元
                HSSFRow row = sheet.createRow(0);
                HSSFCell cell;
                for (int h = 0; h < heads.length; h++) {
                    cell = row.createCell(h);
                    cell.setCellValue(heads[h]);
                    cell.setCellStyle(headCellStyle);
                }
                // 设置表内容单元
                List<ColumnInfo> columnInfoList = jdbcTemplate.query(QUERY_COLUMN_INFO_SQL, new String[]{tableInfo.getTableName(), params.getDbName()}, new BeanPropertyRowMapper<>(ColumnInfo.class));
                for (int i = 0; i < columnInfoList.size(); i++) {
                    row = sheet.createRow(i + 1);
                    String[] cellVals = cellVals(columnInfoList.get(i));
                    for (int n = 0; n < cellVals.length; n ++) {
                        HSSFCell rowCell = row.createCell(n);
                        rowCell.setCellValue(cellVals[n]);
                        rowCell.setCellStyle(n == 0 || n == cellVals.length - 1 ? aCellStyle : bCellStyle);
                    }
                }
                // 列宽自适应
                for (int i = 0; i < heads.length; i++) {
                    sheet.autoSizeColumn(i);
                    sheet.setColumnWidth(i,sheet.getColumnWidth(i)*16/10);
                }
            }
        }
        FileUtils.createFile(response, workbook);
    }

    /**
     * 统一设置单元格边框为实线
     * @param cellStyle 单元格样式对象
     */
    private void uniBorderStyleWithThin(CellStyle cellStyle) {
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
    }

    /**
     * 返回单元内容值数组
     * @param columnInfo 表字段对象
     * @return String[]
     */
    private String[] cellVals(ColumnInfo columnInfo) {
        String[] cellVals = new String[6];
        cellVals[0] = columnInfo.getColumnName();
        cellVals[1] = columnInfo.getColumnType();
        cellVals[2] = columnInfo.getColumnKey();
        cellVals[3] = columnInfo.getIsNullable();
        cellVals[4] = columnInfo.getColumnDefault();
        cellVals[5] = columnInfo.getColumnComment();
        return cellVals;
    }

    /**
     * 获取JdbcTemplate实例
     * @param params 链接参数
     * @return JdbcTemplate
     */
    private JdbcTemplate getJdbcTemplate(ConnectionParams params) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(DataSourceUtils.init(params.getIp(), params.getPort(), params.getUsername(), params.getPassword(), params.getDbName()));
        return jdbcTemplate;
    }
}
