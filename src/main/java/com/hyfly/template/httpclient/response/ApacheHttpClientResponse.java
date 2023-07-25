package com.hyfly.template.httpclient.response;

import lombok.Getter;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.utils.HttpClientUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Apache HttpClient响应实现类
 */
@Getter
public class ApacheHttpClientResponse implements HttpClientResponse {

    private final HttpResponse response;
    private Map<String, String> responseHeaders;

    public ApacheHttpClientResponse(HttpResponse response) {
        this.response = response;
    }

    @Override
    public Map<String, String> getHeaders() {
        if (this.responseHeaders == null) {
            responseHeaders = new LinkedHashMap<>();
            for (Header header : this.response.getAllHeaders()) {
                responseHeaders.put(header.getName(), header.getValue());
            }
        }
        return this.responseHeaders;
    }

    @Override
    public InputStream getBody() throws IOException {
        return response.getEntity() != null ? response.getEntity().getContent() : null;
    }

    @Override
    public int getStatusCode() {
        return this.response.getStatusLine().getStatusCode();
    }

    @Override
    public void close() throws IOException {
        if (this.response != null) {
            HttpClientUtils.closeQuietly(this.response);
        }
    }
}
