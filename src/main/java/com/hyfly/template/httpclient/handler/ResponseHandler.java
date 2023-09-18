package com.hyfly.template.httpclient.handler;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.hyfly.template.httpclient.json.JsonProcessor;
import com.hyfly.template.httpclient.json.JsonProcessorFactory;
import com.hyfly.template.httpclient.model.HttpRestResult;
import com.hyfly.template.httpclient.response.HttpClientResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.io.InputStreamReader;

/**
 * HTTP响应处理器
 *
 * @param <T> 响应数据类型
 */
@Slf4j
public class ResponseHandler<T> {

    private Class<T> responseType;
    private final JsonProcessor jsonProcessor;

    public ResponseHandler() {
        this.jsonProcessor = JsonProcessorFactory.getDefaultProcessor();
    }

    public ResponseHandler(JsonProcessor jsonProcessor) {
        this.jsonProcessor = jsonProcessor;
    }

    public void setResponseType(Class<T> responseType) {
        this.responseType = responseType;
    }

    /**
     * 处理HTTP响应
     *
     * @param response HTTP响应
     * @return 处理结果
     * @throws Exception 异常
     */
    public HttpRestResult<T> handle(HttpClientResponse response) throws Exception {
        if (response.getStatusCode() >= HttpStatus.SC_BAD_REQUEST) {
            return handleError(response);
        }
        return convertResult(response, this.responseType);
    }

    /**
     * 处理错误响应
     */
    private HttpRestResult<T> handleError(HttpClientResponse response) throws Exception {
        String message = CharStreams.toString(
                new InputStreamReader(response.getBody(), Charsets.UTF_8));
        log.error("HTTP请求失败，状态码: {}, 响应内容: {}", response.getStatusCode(), message);
        return HttpRestResult.failure(response.getStatusCode(), message);
    }

    /**
     * 转换响应结果
     */
    @SuppressWarnings("unchecked")
    private HttpRestResult<T> convertResult(HttpClientResponse response, Class<T> responseType)
            throws Exception {
        String responseBody = CharStreams.toString(
                new InputStreamReader(response.getBody(), Charsets.UTF_8));

        if (responseType == String.class) {
            return HttpRestResult.success((T) responseBody);
        }

        try {
            T data = jsonProcessor.parseObject(responseBody, responseType);
            return HttpRestResult.success(data);
        } catch (Exception e) {
            log.error("使用{}解析响应数据失败: {}", jsonProcessor.getProcessorName(), responseBody, e);
            return HttpRestResult.failure(500, "解析响应数据失败: " + e.getMessage());
        }
    }
}
