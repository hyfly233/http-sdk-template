package com.hyfly.http.sdk.remote;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Query {

    private final Map<String, String> params;

    public Query() {
        params = new LinkedHashMap<>();
    }

    public static URI buildUri(String url, Query query) throws Exception {
        if (query != null && !query.isEmpty()) {
            url = url + "?" + query.toQueryUrl();
        }
        return new URI(url);
    }

    public Query addParam(String key, String value) {
        if (key != null && value != null) {
            params.put(key, value);
        }
        return this;
    }

    public Query build() {
        return this;
    }

    public String getValue(String key) {
        return params.get(key);
    }

    public boolean isEmpty() {
        return params.isEmpty();
    }

    public String toQueryUrl() {
        return params.entrySet()
                .stream()
                .filter(entry -> entry.getValue() != null)
                .map(i -> {
                    String encode;
                    try {
                        encode = URLEncoder.encode(i.getValue(), StandardCharsets.UTF_8.displayName());
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                    return i.getKey() + "=" + encode;
                })
                .collect(Collectors.joining("&"));
    }
}
