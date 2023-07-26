package com.hyfly.template.httpclient.response;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * HTTP响应接口
 */
public interface HttpClientResponse {

    /**
     * 获取响应头
     *
     * @return 响应头Map
     */
    Map<String, String> getHeaders();

    /**
     * 获取响应体
     *
     * @return 响应体输入流
     * @throws IOException IO异常
     */
    InputStream getBody() throws IOException;

    /**
     * 获取状态码
     *
     * @return 状态码
     */
    int getStatusCode();

    /**
     * 关闭响应
     *
     * @throws IOException IO异常
     */
    void close() throws IOException;
}
