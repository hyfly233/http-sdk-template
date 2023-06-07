package com.hyfly.template.sdk.core;

import com.hyfly.template.sdk.remote.HttpSdkRestTemplate;

public abstract class BaseOperation {

    protected final String url;

    protected final String token;

    protected final HttpSdkRestTemplate restTemplate;

    public BaseOperation(String url, String token, HttpSdkRestTemplate restTemplate) {
        this.url = url;
        this.token = token;
        this.restTemplate = restTemplate;
    }

//    protected Header getHeader() {
//        Header header = Header.newInstance();
//        header.addParam("token", this.token);
//        return header;
//    }

}
