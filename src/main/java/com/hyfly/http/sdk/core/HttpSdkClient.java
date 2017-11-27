package com.hyfly.http.sdk.core;

import com.hyfly.http.sdk.remote.HttpSdkRestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpSdkClient {

    Logger logger = LoggerFactory.getLogger(HttpSdkClient.class);

    private final String url;
    private final String token;
    private final HttpSdkRestTemplate restTemplate;

    public HttpSdkClient(String url, String token, HttpSdkRestTemplate restTemplate) {
        this.url = url;
        this.token = token;
        this.restTemplate = restTemplate;
        this.initOperators();
    }

    public void initOperators() {

    }
}
