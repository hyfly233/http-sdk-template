package com.hyfly.http.sdk.remote.request;

import com.hyfly.http.sdk.remote.response.DefaultHttpClientResponse;
import com.hyfly.http.sdk.remote.response.HttpClientResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.net.URI;

public class DefaultHttpClientRequest implements HttpClientRequest {

    private final CloseableHttpClient client;

    private final RequestConfig requestConfig;

    public DefaultHttpClientRequest(CloseableHttpClient client, RequestConfig requestConfig) {
        this.client = client;
        this.requestConfig = requestConfig;
    }

    @Override
    public HttpClientResponse execute(URI uri, String httpMethod, RequestHttpEntity requestHttpEntity) throws Exception {
        HttpRequestBase request = build(uri, httpMethod, requestHttpEntity);
        CloseableHttpResponse closeableHttpResponse = client.execute(request);
        return new DefaultHttpClientResponse(closeableHttpResponse);
    }

    @Override
    public void close() throws IOException {
        client.close();
    }

    private HttpRequestBase build(URI uri, String httpMethod, RequestHttpEntity requestHttpEntity) throws Exception {
        requestHttpEntity.getBody();


        return null;
    }
}
