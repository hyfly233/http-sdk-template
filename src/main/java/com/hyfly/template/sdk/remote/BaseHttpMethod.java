package com.hyfly.template.sdk.remote;

import org.apache.http.client.methods.*;

public enum BaseHttpMethod {
    GET(HttpMethod.GET) {
        @Override
        protected HttpRequestBase createRequest(String url) {
            return new HttpGet(url);
        }
    },

    POST(HttpMethod.POST) {
        @Override
        protected HttpRequestBase createRequest(String url) {
            return new HttpPost(url);
        }
    },

    PUT(HttpMethod.PUT) {
        @Override
        protected HttpRequestBase createRequest(String url) {
            return new HttpPut(url);
        }
    },

    PATCH(HttpMethod.PATCH) {
        @Override
        protected HttpRequestBase createRequest(String url) {
            return new HttpPatch(url);
        }
    },

    DELETE(HttpMethod.DELETE) {
        @Override
        protected HttpRequestBase createRequest(String url) {
            return new HttpDelete(url);
        }
    };

    private final String name;

    BaseHttpMethod(String name) {
        this.name = name;
    }

    public static BaseHttpMethod of(String name) {
        if (name != null && !name.isEmpty()) {
            for (BaseHttpMethod method : BaseHttpMethod.values()) {
                if (name.equalsIgnoreCase(method.name)) {
                    return method;
                }
            }
        }
        throw new IllegalArgumentException("Unsupported http method : " + name);
    }

    public HttpRequestBase init(String url) {
        return createRequest(url);
    }

    protected HttpRequestBase createRequest(String url) {
        throw new UnsupportedOperationException();
    }
}
