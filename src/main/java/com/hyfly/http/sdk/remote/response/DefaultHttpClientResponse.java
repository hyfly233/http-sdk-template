package com.hyfly.http.sdk.remote.response;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.utils.HttpClientUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultHttpClientResponse implements HttpClientResponse {

    private final HttpResponse response;

    private Map<String, String> responseHeaders;

    public DefaultHttpClientResponse(HttpResponse response) {
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
        return response.getEntity().getContent();
    }

    @Override
    public int getStatusCode() {
        return this.response.getStatusLine().getStatusCode();
    }

    @Override
    public void close() throws IOException {
        if (this.response != null) {
            HttpClientUtils.closeQuietly(response);
        }
    }
}
