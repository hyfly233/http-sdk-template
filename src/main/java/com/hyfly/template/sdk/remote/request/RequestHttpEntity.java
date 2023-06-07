package com.hyfly.template.sdk.remote.request;

import com.alibaba.fastjson2.JSONObject;
import com.hyfly.template.sdk.remote.Header;
import com.hyfly.template.sdk.remote.Query;

import java.util.Map;

public class RequestHttpEntity {

    private Header header;

    private Query query;

    private Object body;

    public RequestHttpEntity() {
    }

    public RequestHttpEntity(Header header, Query query) {
        this.header = header;
        this.query = query;
    }

    public RequestHttpEntity(Header header, Query query, Object body) {
        this.header = header;
        this.query = query;
        this.body = body;
    }

    public RequestHttpEntity(Header header, Object body) {
        this.header = header;
        this.body = body;
    }

    public Map<String, Object> castBodyToMap() {
        if (body instanceof Map) {
            return (Map<String, Object>) body;
        }
        throw new UnsupportedOperationException(
                "the body is not instance of map,do not use this method");
    }

    public String bodyToJson() {
        return JSONObject.toJSONString(this.body);
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
