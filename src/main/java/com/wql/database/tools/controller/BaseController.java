package com.wql.database.tools.controller;

import com.alibaba.fastjson.JSONObject;
import com.wql.database.tools.domain.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 基类
 */
@Slf4j
public class BaseController {

    /**
     * 处理验证错误信息
     * @param response 响应对象
     * @param result   错误结果
     * @return boolean
     */
    public boolean handleValidatedError(HttpServletResponse response, BindingResult result) throws IOException {
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
            log.error(">>> 参数验证错误：{}", errorMessage);
            response.setCharacterEncoding("GBK");
            ResponseMessage<Object> responseMessage = new ResponseMessage<>();
            responseMessage.setErrorCode(500);
            responseMessage.setErrorMessage(errorMessage);
            PrintWriter writer = response.getWriter();
            writer.write(JSONObject.toJSONString(responseMessage));
            writer.flush();
            writer.close();
            return true;
        }
        return false;
    }
}
