package com.hyfly.template.httpclient.core;

import com.google.common.net.MediaType;
import com.hyfly.template.httpclient.constants.HttpMethod;
import com.hyfly.template.httpclient.handler.ResponseHandler;
import com.hyfly.template.httpclient.model.*;
import com.hyfly.template.httpclient.request.HttpClientRequest;
import com.hyfly.template.httpclient.response.HttpClientResponse;
import com.hyfly.template.httpclient.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;

import java.net.URI;

/**
 * HTTP REST模板类 - 支持各种HTTP操作的统一接口
 */
@Slf4j
public class HttpRestTemplate {

    private final HttpClientRequest requestClient;

    public HttpRestTemplate(HttpClientRequest requestClient) {
        this.requestClient = requestClient;
    }

    /**
     * GET请求
     *
     * @param url          请求URL
     * @param header       请求头
     * @param query        查询参数
     * @param responseType 响应类型
     * @param <T>          响应数据类型
     * @return 响应结果
     * @throws Exception 异常
     */
    public <T> HttpRestResult<T> get(String url, Header header, Query query, Class<T> responseType)
            throws Exception {
        return execute(url, HttpMethod.GET, new RequestHttpEntity(header, query), responseType);
    }

    /**
     * POST表单请求
     *
     * @param url          请求URL
     * @param header       请求头
     * @param body         请求体
     * @param responseType 响应类型
     * @param <T>          响应数据类型
     * @return 响应结果
     * @throws Exception 异常
     */
    public <T> HttpRestResult<T> postForm(String url, Header header, Object body, Class<T> responseType)
            throws Exception {
        RequestHttpEntity requestHttpEntity =
                new RequestHttpEntity(header.setContentType(MediaType.FORM_DATA.toString()), body);
        return execute(url, HttpMethod.POST, requestHttpEntity, responseType);
    }

    /**
     * POST表单请求（带查询参数）
     *
     * @param url          请求URL
     * @param header       请求头
     * @param query        查询参数
     * @param body         请求体
     * @param responseType 响应类型
     * @param <T>          响应数据类型
     * @return 响应结果
     * @throws Exception 异常
     */
    public <T> HttpRestResult<T> postForm(String url, Header header, Query query, Object body,
                                          Class<T> responseType) throws Exception {
        RequestHttpEntity requestHttpEntity =
                new RequestHttpEntity(header.setContentType(MediaType.FORM_DATA.toString()), query, body);
        return execute(url, HttpMethod.POST, requestHttpEntity, responseType);
    }

    /**
     * POST文件表单请求（支持文件上传）
     *
     * @param url          请求URL
     * @param header       请求头
     * @param body         请求体
     * @param responseType 响应类型
     * @param <T>          响应数据类型
     * @return 响应结果
     * @throws Exception 异常
     */
    public <T> HttpRestResult<T> postFileForm(String url, Header header, Object body, Class<T> responseType)
            throws Exception {
        RequestHttpEntity requestHttpEntity =
                new RequestHttpEntity(header.setContentType(ContentType.MULTIPART_FORM_DATA.getMimeType()), body);
        return execute(url, HttpMethod.POST, requestHttpEntity, responseType);
    }

    /**
     * POST JSON请求
     *
     * @param url          请求URL
     * @param header       请求头
     * @param body         请求体
     * @param responseType 响应类型
     * @param <T>          响应数据类型
     * @return 响应结果
     * @throws Exception 异常
     */
    public <T> HttpRestResult<T> postJson(String url, Header header, Object body, Class<T> responseType)
            throws Exception {
        RequestHttpEntity requestHttpEntity =
                new RequestHttpEntity(header.setContentType(MediaType.JSON_UTF_8.toString()), body);
        return execute(url, HttpMethod.POST, requestHttpEntity, responseType);
    }

    /**
     * POST JSON请求（带查询参数）
     *
     * @param url          请求URL
     * @param header       请求头
     * @param query        查询参数
     * @param body         请求体
     * @param responseType 响应类型
     * @param <T>          响应数据类型
     * @return 响应结果
     * @throws Exception 异常
     */
    public <T> HttpRestResult<T> postJson(String url, Header header, Query query, Object body,
                                          Class<T> responseType) throws Exception {
        RequestHttpEntity requestHttpEntity =
                new RequestHttpEntity(header.setContentType(MediaType.JSON_UTF_8.toString()), query, body);
        return execute(url, HttpMethod.POST, requestHttpEntity, responseType);
    }

    /**
     * PUT表单请求
     *
     * @param url          请求URL
     * @param header       请求头
     * @param body         请求体
     * @param responseType 响应类型
     * @param <T>          响应数据类型
     * @return 响应结果
     * @throws Exception 异常
     */
    public <T> HttpRestResult<T> putForm(String url, Header header, Object body, Class<T> responseType)
            throws Exception {
        RequestHttpEntity requestHttpEntity =
                new RequestHttpEntity(header.setContentType(MediaType.FORM_DATA.toString()), body);
        return execute(url, HttpMethod.PUT, requestHttpEntity, responseType);
    }

    /**
     * PUT表单请求（带查询参数）
     *
     * @param url          请求URL
     * @param header       请求头
     * @param query        查询参数
     * @param body         请求体
     * @param responseType 响应类型
     * @param <T>          响应数据类型
     * @return 响应结果
     * @throws Exception 异常
     */
    public <T> HttpRestResult<T> putForm(String url, Header header, Query query, Object body,
                                         Class<T> responseType) throws Exception {
        RequestHttpEntity requestHttpEntity =
                new RequestHttpEntity(header.setContentType(MediaType.FORM_DATA.toString()), query, body);
        return execute(url, HttpMethod.PUT, requestHttpEntity, responseType);
    }

    /**
     * PUT JSON请求
     *
     * @param url          请求URL
     * @param header       请求头
     * @param body         请求体
     * @param responseType 响应类型
     * @param <T>          响应数据类型
     * @return 响应结果
     * @throws Exception 异常
     */
    public <T> HttpRestResult<T> putJson(String url, Header header, Object body, Class<T> responseType)
            throws Exception {
        RequestHttpEntity requestHttpEntity =
                new RequestHttpEntity(header.setContentType(MediaType.JSON_UTF_8.toString()), body);
        return execute(url, HttpMethod.PUT, requestHttpEntity, responseType);
    }

    /**
     * PUT JSON请求（带查询参数）
     *
     * @param url          请求URL
     * @param header       请求头
     * @param query        查询参数
     * @param body         请求体
     * @param responseType 响应类型
     * @param <T>          响应数据类型
     * @return 响应结果
     * @throws Exception 异常
     */
    public <T> HttpRestResult<T> putJson(String url, Header header, Query query, Object body,
                                         Class<T> responseType) throws Exception {
        RequestHttpEntity requestHttpEntity =
                new RequestHttpEntity(header.setContentType(MediaType.JSON_UTF_8.toString()), query, body);
        return execute(url, HttpMethod.PUT, requestHttpEntity, responseType);
    }

    /**
     * DELETE请求
     *
     * @param url          请求URL
     * @param header       请求头
     * @param query        查询参数
     * @param responseType 响应类型
     * @param <T>          响应数据类型
     * @return 响应结果
     * @throws Exception 异常
     */
    public <T> HttpRestResult<T> delete(String url, Header header, Query query, Class<T> responseType)
            throws Exception {
        RequestHttpEntity requestHttpEntity =
                new RequestHttpEntity(header.setContentType(MediaType.FORM_DATA.toString()), query);
        return execute(url, HttpMethod.DELETE, requestHttpEntity, responseType);
    }

    /**
     * SSE流式请求
     * 用于处理Server-Sent Events响应
     *
     * @param url SSE接口URL
     * @param header 请求头
     * @param query 查询参数
     * @return HttpClientResponse 原始响应对象，用于流式处理
     * @throws Exception 异常
     */
    public HttpClientResponse sseStream(String url, Header header, Query query) throws Exception {
        Header sseHeader = header != null ? header : new Header();
        // 设置SSE必需的请求头
        sseHeader.add("Accept", "text/event-stream");
        sseHeader.add("Cache-Control", "no-cache");

        RequestHttpEntity requestHttpEntity = new RequestHttpEntity(sseHeader, query);
        URI uri = HttpUtils.buildUri(url, requestHttpEntity.getQuery());

        log.debug("执行SSE请求: {}", uri);
        // 注意：SSE响应不能自动关闭，需要调用者管理
        return this.requestClient.execute(uri, HttpMethod.GET, requestHttpEntity);
    }

    public HttpClientResponse sseStream(String url) throws Exception {
        return sseStream(url, null, null);
    }

    public HttpClientResponse sseStream(String url, Header header) throws Exception {
        return sseStream(url, header, null);
    }

    public HttpClientResponse sseStream(String url, Query query) throws Exception {
        return sseStream(url, null, query);
    }

    /**
     * 执行HTTP请求的核心方法
     */
    private <T> HttpRestResult<T> execute(String url, String httpMethod, RequestHttpEntity requestEntity,
                                          Class<T> responseType) throws Exception {
        URI uri = HttpUtils.buildUri(url, requestEntity.getQuery());

        ResponseHandler<T> responseHandler = new ResponseHandler<>();
        responseHandler.setResponseType(responseType);

        HttpClientResponse response = null;
        try {
            response = this.requestClient.execute(uri, httpMethod, requestEntity);
            return responseHandler.handle(response);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
}
