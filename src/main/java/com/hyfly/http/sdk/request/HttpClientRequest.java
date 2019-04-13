package com.hyfly.http.sdk.request;

import com.hyfly.http.sdk.response.HttpClientResponse;

import java.io.Closeable;
import java.net.URI;

public interface HttpClientRequest extends Closeable {

    HttpClientResponse execute(URI uri, String httpMethod, RequestHttpEntity requestHttpEntity)
            throws Exception;
}
