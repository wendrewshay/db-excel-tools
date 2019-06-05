package com.wql.database.tools.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wql.database.tools.domain.ConnectionParams;
import com.wql.database.tools.domain.ResponseMessage;
import com.wql.database.tools.entity.ColumnInfo;
import com.wql.database.tools.entity.TableInfo;
import com.wql.database.tools.service.SchemaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

/**
 * 主页
 * Created by wendrewshay on 2019/05/31
 */
@Slf4j
@Controller
public class HomeController extends BaseController{

    @Autowired
    private SchemaService schemaService;

    /**
     * 主页面
     * @return String
     */
    @GetMapping("/")
    public String home() {
        return "home";
    }

    /**
     * 查询所有表元信息
     * @param params 连接参数
     * @return List<TableInfo>
     */
    @GetMapping(value = "/queryTables")
    @ResponseBody
    public ResponseMessage queryTables(HttpServletResponse response, @Validated ConnectionParams params, BindingResult result) throws IOException {
        log.info(">>> 请求参数：{}", JSONObject.toJSONString(params));
        if (handleValidatedError(response, result)) {
            return null;
        }
        List list = schemaService.queryTables(params);
        return new ResponseMessage(200, "成功", list);
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
     * @param result   错误绑定对象
     */
    @GetMapping(value = "export")
    public void export(HttpServletResponse response, @Validated ConnectionParams params, BindingResult result) throws IOException {
        log.info(">>> 请求参数：{}", JSONObject.toJSONString(params));
        if (handleValidatedError(response, result)) {
            return;
        }
        schemaService.handle(response, params);
    }

    /**
     * 导出数据库中指定表的字段信息到Excel
     * @param response 响应对象
     * @param params   数据库连接参数
     * @param result   错误绑定对象
     */
    @GetMapping(value = "exportWithAssignedTables")
    public void exportWithAssignedTables(HttpServletResponse response, @RequestParam(value = "tablesJson") String tablesJson, @Validated ConnectionParams params, BindingResult result) throws IOException {
        log.info(">>> 请求参数params = {}, tablesJson = {}", JSONObject.toJSONString(params), tablesJson);
        if (handleValidatedError(response, result)) {
            return;
        }
        List<TableInfo> tableInfos = JSONArray.parseArray(tablesJson, TableInfo.class);
        params.setTableList(tableInfos);
        schemaService.handle(response, params);
    }
}
