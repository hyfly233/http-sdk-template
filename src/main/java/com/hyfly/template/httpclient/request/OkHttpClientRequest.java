package com.hyfly.template.httpclient.request;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import com.hyfly.template.httpclient.model.RequestHttpEntity;
import com.hyfly.template.sdk.remote.response.HttpClientResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.http.entity.ContentType;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * OkHttp客户端请求实现类
 */
@Slf4j
public class OkHttpClientRequest implements HttpClientRequest {

    private final OkHttpClient client;

    public OkHttpClientRequest(OkHttpClient client) {
        this.client = client;
    }

    public OkHttpClientRequest(int connectTimeout, int readTimeout, int writeTimeout) {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS)
                .build();
    }

    @Override
    public HttpClientResponse execute(URI uri, String httpMethod, RequestHttpEntity requestHttpEntity)
            throws Exception {
        Request request = buildRequest(uri, httpMethod, requestHttpEntity);
        Response response = client.newCall(request).execute();
        return new OkHttpClientResponse(response);
    }

    /**
     * 构建OkHttp请求
     */
    private Request buildRequest(URI uri, String httpMethod, RequestHttpEntity requestHttpEntity)
            throws Exception {
        Request.Builder requestBuilder = new Request.Builder().url(uri.toString());

        // 设置请求头
        Header headers = requestHttpEntity.getHeader();
        if (headers != null && !headers.isEmpty()) {
            Iterator<Map.Entry<String, String>> iterator = headers.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        // 根据HTTP方法和Content-Type构建请求体
        RequestBody requestBody = buildRequestBody(requestHttpEntity);

        switch (httpMethod.toUpperCase()) {
            case "GET":
                requestBuilder.get();
                break;
            case "POST":
                requestBuilder.post(requestBody != null ? requestBody : RequestBody.create("", null));
                break;
            case "PUT":
                requestBuilder.put(requestBody != null ? requestBody : RequestBody.create("", null));
                break;
            case "DELETE":
                if (requestBody != null) {
                    requestBuilder.delete(requestBody);
                } else {
                    requestBuilder.delete();
                }
                break;
            case "PATCH":
                requestBuilder.patch(requestBody != null ? requestBody : RequestBody.create("", null));
                break;
            default:
                throw new IllegalArgumentException("不支持的HTTP方法: " + httpMethod);
        }

        return requestBuilder.build();
    }

    /**
     * 构建请求体
     */
    private RequestBody buildRequestBody(RequestHttpEntity requestHttpEntity) throws Exception {
        Object body = requestHttpEntity.getBody();
        if (body == null) {
            return null;
        }

        Header headers = requestHttpEntity.getHeader();
        String contentType = headers != null ? headers.getValue(HttpHeaders.CONTENT_TYPE) : null;

        if (MediaType.FORM_DATA.toString().equals(contentType)) {
            return buildFormRequestBody(requestHttpEntity);
        } else if (ContentType.MULTIPART_FORM_DATA.getMimeType().equals(contentType)) {
            return buildMultipartRequestBody(requestHttpEntity);
        } else {
            return buildJsonRequestBody(body, contentType);
        }
    }

    /**
     * 构建表单请求体
     */
    private RequestBody buildFormRequestBody(RequestHttpEntity requestHttpEntity) {
        Map<String, Object> formData = getFormData(requestHttpEntity);
        if (formData == null || formData.isEmpty()) {
            return null;
        }

        FormBody.Builder formBuilder = new FormBody.Builder();
        for (Map.Entry<String, Object> entry : formData.entrySet()) {
            formBuilder.add(entry.getKey(), entry.getValue().toString());
        }

        return formBuilder.build();
    }

    /**
     * 构建多部分请求体（文件上传）
     */
    private RequestBody buildMultipartRequestBody(RequestHttpEntity requestHttpEntity) {
        Map<String, Object> formData = getFormData(requestHttpEntity);
        if (formData == null || formData.isEmpty()) {
            return null;
        }

        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        for (Map.Entry<String, Object> entry : formData.entrySet()) {
            if (entry.getValue() instanceof File) {
                File file = (File) entry.getValue();
                RequestBody fileBody = RequestBody.create(file,
                        okhttp3.MediaType.parse("application/octet-stream"));
                multipartBuilder.addFormDataPart(entry.getKey(), file.getName(), fileBody);
            } else {
                multipartBuilder.addFormDataPart(entry.getKey(), entry.getValue().toString());
            }
        }

        return multipartBuilder.build();
    }

    /**
     * 构建JSON请求体
     */
    private RequestBody buildJsonRequestBody(Object body, String contentType) {
        String jsonString;
        if (body instanceof String) {
            jsonString = (String) body;
        } else if (body instanceof byte[]) {
            return RequestBody.create((byte[]) body,
                    okhttp3.MediaType.parse("application/json; charset=utf-8"));
        } else {
            jsonString = JSONObject.toJSONString(body);
        }

        okhttp3.MediaType mediaType = contentType != null ?
                okhttp3.MediaType.parse(contentType) :
                okhttp3.MediaType.parse("application/json; charset=utf-8");

        return RequestBody.create(jsonString, mediaType);
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
        // OkHttpClient doesn't need explicit closing
        // The connection pool will be managed automatically
    }
}
