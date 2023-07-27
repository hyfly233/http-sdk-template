package com.hyfly.template.httpclient.response;

import lombok.Getter;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * OkHttp响应实现类
 */
@Getter
public class OkHttpClientResponse implements HttpClientResponse {

    private final Response response;
    private Map<String, String> responseHeaders;

    public OkHttpClientResponse(Response response) {
        this.response = response;
    }

    @Override
    public Map<String, String> getHeaders() {
        if (this.responseHeaders == null) {
            responseHeaders = new HashMap<>();
            response.headers().forEach(pair ->
                responseHeaders.put(pair.getFirst(), pair.getSecond()));
        }
        return this.responseHeaders;
    }

    @Override
    public InputStream getBody() throws IOException {
        ResponseBody body = response.body();
        return body != null ? body.byteStream() : null;
    }

    @Override
    public int getStatusCode() {
        return response.code();
    }

    @Override
    public void close() throws IOException {
        if (response != null) {
            response.close();
        }
    }
}
