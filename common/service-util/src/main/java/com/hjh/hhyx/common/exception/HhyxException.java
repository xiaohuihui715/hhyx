package com.hjh.hhyx.common.exception;

import com.hjh.hhyx.common.result.ResultCodeEnum;
import lombok.Data;

/**
 * @author 韩
 * @version 1.0
 * 自定义异常
 */
@Data
public class HhyxException extends RuntimeException{

    //异常状态码
    private Integer code;

    /**
     * 通过状态码和错误消息创建异常对象
     * @param message
     * @param code
     */
    public HhyxException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    /**
     * 接收枚举类型对象
     * @param resultCodeEnum
     */
    public HhyxException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "HHyxException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}
