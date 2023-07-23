package com.hyfly.template.httpclient.request;


import com.hyfly.template.httpclient.model.RequestHttpEntity;
import com.hyfly.template.sdk.remote.response.HttpClientResponse;

import java.io.Closeable;
import java.net.URI;

/**
 * HTTP客户端请求接口
 */
public interface HttpClientRequest extends Closeable {

    /**
     * 执行HTTP请求
     *
     * @param uri               请求URI
     * @param httpMethod        HTTP方法
     * @param requestHttpEntity 请求实体
     * @return HTTP响应
     * @throws Exception 异常
     */
    HttpClientResponse execute(URI uri, String httpMethod, RequestHttpEntity requestHttpEntity)
            throws Exception;
}
