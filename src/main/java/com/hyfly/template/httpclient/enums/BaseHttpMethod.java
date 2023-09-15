package com.hyfly.template.httpclient.enums;

import com.google.common.base.Strings;
import com.hyfly.template.httpclient.constants.HttpMethod;
import org.apache.http.client.methods.*;

/**
 * HTTP方法枚举
 */
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

    /**
     * 根据名称获取HTTP方法
     *
     * @param name 方法名称
     * @return BaseHttpMethod
     */
    public static BaseHttpMethod of(String name) {
        if (!Strings.isNullOrEmpty(name)) {
            for (BaseHttpMethod method : BaseHttpMethod.values()) {
                if (name.equalsIgnoreCase(method.name)) {
                    return method;
                }
            }
        }
        throw new IllegalArgumentException("不支持的HTTP方法: " + name);
    }

    public HttpRequestBase init(String url) {
        return createRequest(url);
    }

    protected HttpRequestBase createRequest(String url) {
        throw new UnsupportedOperationException();
    }
}
