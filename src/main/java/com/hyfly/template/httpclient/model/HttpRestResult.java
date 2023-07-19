package com.hyfly.template.httpclient.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * HTTP响应结果封装类
 *
 * @param <T> 响应数据类型
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpRestResult<T> {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 是否失败
     */
    private Boolean failed;

    /**
     * 创建成功响应
     *
     * @param data 响应数据
     * @param <T>  数据类型
     * @return HttpRestResult
     */
    public static <T> HttpRestResult<T> success(T data) {
        HttpRestResult<T> result = new HttpRestResult<>();
        result.setCode(200);
        result.setMessage("成功");
        result.setData(data);
        result.setSuccess(true);
        result.setFailed(false);
        return result;
    }

    /**
     * 创建失败响应
     *
     * @param code    错误码
     * @param message 错误消息
     * @param <T>     数据类型
     * @return HttpRestResult
     */
    public static <T> HttpRestResult<T> failure(Integer code, String message) {
        HttpRestResult<T> result = new HttpRestResult<>();
        result.setCode(code);
        result.setMessage(message);
        result.setSuccess(false);
        result.setFailed(true);
        return result;
    }

    /**
     * 判断是否成功
     *
     * @return 是否成功
     */
    public boolean isSuccess() {
        return success != null && success;
    }
}
