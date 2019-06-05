package com.wql.database.tools.domain;

import lombok.Data;

/**
 * 响应信息封装
 * Created by wendrewshay on 2019/06/05 10:06
 */
@Data
public class ResponseMessage<T> {

    private int errorCode;

    private String errorMessage;

    private T data;

    public ResponseMessage() {
    }

    public ResponseMessage(int errorCode, String errorMessage, T data) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.data = data;
    }
}
