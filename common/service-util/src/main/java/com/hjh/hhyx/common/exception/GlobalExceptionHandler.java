package com.hjh.hhyx.common.exception;

import com.hjh.hhyx.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 韩
 * @version 1.0
 * 全局异常处理器
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.fail(null);
    }

    /**
     * 自定义异常处理器
     *
     * @param hhyxException
     * @return
     */
    @ExceptionHandler(HhyxException.class)
    @ResponseBody
    public Result error(HhyxException hhyxException) {
        return Result.fail(null);
    }
}