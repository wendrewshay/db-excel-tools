package com.wql.database.tools.controller;

import com.wql.database.tools.domain.ConnectionParams;
import com.wql.database.tools.entity.ColumnInfo;
import com.wql.database.tools.service.SchemaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * 主页
 * Created by wendrewshay on 2019/05/31
 */
@Slf4j
@Controller
public class HomeController {

    @Autowired
    private SchemaService schemaService;

    /**
     * 查询所有表元信息
     * @param params 连接参数
     * @return List<TableInfo>
     */
    @GetMapping(value = "/queryTables")
    @ResponseBody
    public List queryTables(HttpServletResponse response, @Validated ConnectionParams params, BindingResult result) throws IOException {
        if (handleValidatedError(response, result)) {
            return null;
        }
        return schemaService.queryTables(params);
    }

    /**
     * 查询表字段信息
     * @param params    连接参数
     * @param tableName 表名称
     * @return ColumnInfo
     */
    @GetMapping(value = "/queryColumnsOf/{tableName}")
    @ResponseBody
    public List<ColumnInfo> queryColumnInfo(HttpServletResponse response, @Validated ConnectionParams params,
                                            @PathVariable(name = "tableName") String tableName, BindingResult result) throws IOException {
        if (handleValidatedError(response, result)) {
            return null;
        }
        return schemaService.queryColumnInfo(params, tableName);
    }

    /**
     * 导出指定数据库中所有表字段信息到Excel
     * @param response 响应对象
     * @param params   数据库连接参数
     */
    @GetMapping(value = "export")
    public void export(HttpServletResponse response, @Validated ConnectionParams params, BindingResult result) throws IOException {
        if (handleValidatedError(response, result)) {
            return;
        }
        schemaService.handle(response, params);
    }

    /**
     * 处理验证错误信息
     * @param response 响应对象
     * @param result   错误结果
     * @return
     */
    private boolean handleValidatedError(HttpServletResponse response, BindingResult result) throws IOException {
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
            log.error(">>> 参数验证错误：{}", errorMessage);
            response.setCharacterEncoding("GBK");
            PrintWriter writer = response.getWriter();
            writer.write(errorMessage);
            writer.flush();
            writer.close();
            return true;
        }
        return false;
    }
}
