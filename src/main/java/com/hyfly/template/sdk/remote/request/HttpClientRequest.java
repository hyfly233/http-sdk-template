package com.hyfly.template.sdk.remote.request;

import com.hyfly.template.sdk.remote.response.HttpClientResponse;

import java.io.Closeable;
import java.net.URI;

public interface HttpClientRequest extends Closeable {

    HttpClientResponse execute(URI uri, String httpMethod, RequestHttpEntity requestHttpEntity)
            throws Exception;
}
