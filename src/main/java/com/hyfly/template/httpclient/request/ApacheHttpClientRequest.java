package com.hyfly.template.httpclient.request;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import com.hyfly.template.sdk.remote.request.HttpClientRequest;
import com.template.httpclient.enums.BaseHttpMethod;
import com.template.httpclient.model.Header;
import com.template.httpclient.model.RequestHttpEntity;
import com.template.httpclient.response.ApacheHttpClientResponse;
import com.template.httpclient.response.HttpClientResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.*;

/**
 * Apache HttpClient请求实现类
 */
@Slf4j
public class ApacheHttpClientRequest implements HttpClientRequest {

    private final CloseableHttpClient client;
    private final RequestConfig defaultConfig;

    public ApacheHttpClientRequest(CloseableHttpClient client, RequestConfig defaultConfig) {
        this.client = client;
        this.defaultConfig = defaultConfig;
    }

    @Override
    public HttpClientResponse execute(URI uri, String httpMethod, RequestHttpEntity requestHttpEntity)
            throws Exception {
        HttpRequestBase request = buildRequest(uri, httpMethod, requestHttpEntity);
        CloseableHttpResponse closeableHttpResponse = client.execute(request);
        return new ApacheHttpClientResponse(closeableHttpResponse);
    }

    /**
     * 构建HTTP请求
     */
    private HttpRequestBase buildRequest(URI uri, String httpMethod, RequestHttpEntity requestHttpEntity)
            throws Exception {
        Object body = requestHttpEntity.getBody();
        BaseHttpMethod method = BaseHttpMethod.of(httpMethod);
        Header headers = requestHttpEntity.getHeader();
        final HttpRequestBase requestBase = method.init(uri.toString());

        this.initRequestHeader(requestBase, headers);

        // 根据不同的Content-Type处理请求体
        String contentType = headers.getValue(HttpHeaders.CONTENT_TYPE);

        if (MediaType.FORM_DATA.toString().equals(contentType)) {
            handleFormData(requestBase, requestHttpEntity);
        } else if (ContentType.MULTIPART_FORM_DATA.getMimeType().equals(contentType)) {
            handleMultipartFormData(requestBase, requestHttpEntity);
        } else {
            handleJsonData(requestBase, body, headers);
        }

        requestBase.setConfig(defaultConfig);
        return requestBase;
    }

    /**
     * 初始化请求头
     */
    private void initRequestHeader(HttpRequestBase request, Header headers) {
        if (headers != null && !headers.isEmpty()) {
            Iterator<Map.Entry<String, String>> iterator = headers.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                request.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 处理表单数据
     */
    private void handleFormData(HttpRequestBase requestBase, RequestHttpEntity requestHttpEntity)
            throws Exception {
        Map<String, Object> form = getFormData(requestHttpEntity);

        if (form != null && !form.isEmpty()) {
            List<NameValuePair> params = new ArrayList<>(form.size());
            for (Map.Entry<String, Object> entry : form.entrySet()) {
                params.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            }

            if (requestBase instanceof HttpEntityEnclosingRequest) {
                HttpEntityEnclosingRequest request = (HttpEntityEnclosingRequest) requestBase;
                HttpEntity entity = new UrlEncodedFormEntity(params,
                        requestHttpEntity.getHeader().getCharset());
                request.setEntity(entity);
            }
        }
    }

    /**
     * 处理多部分表单数据（文件上传）
     */
    private void handleMultipartFormData(HttpRequestBase requestBase, RequestHttpEntity requestHttpEntity) {
        Map<String, Object> form = getFormData(requestHttpEntity);

        if (form != null && !form.isEmpty()) {
            MultipartEntityBuilder entityBuilder =
                    MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            for (Map.Entry<String, Object> entry : form.entrySet()) {
                if (entry.getValue() instanceof File) {
                    File file = (File) entry.getValue();
                    entityBuilder.addBinaryBody(entry.getKey(), file,
                            ContentType.DEFAULT_BINARY, file.getName());
                } else {
                    entityBuilder.addTextBody(entry.getKey(), entry.getValue().toString(),
                            ContentType.DEFAULT_TEXT);
                }
            }

            if (requestBase instanceof HttpEntityEnclosingRequest) {
                HttpEntityEnclosingRequest request = (HttpEntityEnclosingRequest) requestBase;
                HttpEntity entity = entityBuilder.build();
                request.setEntity(entity);
            }
        }
    }

    /**
     * 处理JSON数据
     */
    private void handleJsonData(HttpRequestBase requestBase, Object body, Header headers) {
        if (requestBase instanceof HttpEntityEnclosingRequest) {
            HttpEntityEnclosingRequest request = (HttpEntityEnclosingRequest) requestBase;
            ContentType contentType = ContentType.create(MediaType.JSON_UTF_8.type(),
                    headers.getCharset());

            HttpEntity entity;
            if (body instanceof byte[]) {
                entity = new ByteArrayEntity((byte[]) body, contentType);
            } else {
                String jsonString = body instanceof String ?
                        (String) body : JSONObject.toJSONString(body);
                entity = new StringEntity(jsonString, contentType);
            }
            request.setEntity(entity);
        }
    }

    /**
     * 获取表单数据
     */
    private Map<String, Object> getFormData(RequestHttpEntity requestHttpEntity) {
        if (requestHttpEntity.ifBodyIsMap()) {
            return requestHttpEntity.castBodyToMap();
        } else {
            return requestHttpEntity.bodyToMap();
        }
    }

    @Override
    public void close() throws IOException {
        if (client != null) {
            client.close();
        }
    }
}
