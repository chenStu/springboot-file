package com.example.file.utils;

import lombok.Data;

import java.io.Serializable;

/**
 * @author CJS
 * @description
 * @date 14:24 19/10/21
 */
@Data
public class ResultUtil<T> implements Serializable {

    private Integer code;
    private String message;
    private T data;

    public static ResultUtil<Object> success(String msg, Object object) {

        ResultUtil<Object> resultUtil = new ResultUtil<>();
        resultUtil.setCode(205);
        resultUtil.setMessage(msg);
        resultUtil.setData(object);
        return resultUtil;
    }

    public static ResultUtil<Object> error(String msg, Object object) {

        ResultUtil<Object> resultUtil = new ResultUtil<>();
        resultUtil.setCode(500);
        resultUtil.setMessage(msg);
        resultUtil.setData(object);
        return resultUtil;
    }
}
