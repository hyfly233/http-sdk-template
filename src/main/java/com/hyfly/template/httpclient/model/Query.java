package com.hyfly.template.httpclient.model;

import lombok.Getter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * HTTP查询参数封装类
 */
@Getter
public class Query {

    private final Map<String, String> params;

    public Query() {
        params = new LinkedHashMap<>();
    }

    /**
     * 添加查询参数
     *
     * @param key   参数名
     * @param value 参数值
     * @return Query
     */
    public Query addParam(String key, String value) {
        if (key != null && value != null) {
            params.put(key, value);
        }
        return this;
    }

    /**
     * 添加查询参数（Object类型）
     *
     * @param key   参数名
     * @param value 参数值
     * @return Query
     */
    public Query addParam(String key, Object value) {
        if (key != null && value != null) {
            params.put(key, value.toString());
        }
        return this;
    }

    public Query build() {
        return this;
    }

    /**
     * 获取参数值
     *
     * @param key 参数名
     * @return 参数值
     */
    public String getValue(String key) {
        return params.get(key);
    }

    /**
     * 判断是否为空
     *
     * @return 是否为空
     */
    public boolean isEmpty() {
        return params.isEmpty();
    }

    /**
     * 获取所有参数名
     *
     * @return 参数名集合
     */
    public Set<String> getParamNames() {
        return params.keySet();
    }

    /**
     * 转换为查询字符串
     *
     * @return 查询字符串
     */
    public String toQueryUrl() {
        if (isEmpty()) {
            return "";
        }

        StringBuilder queryBuilder = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!first) {
                queryBuilder.append("&");
            } else {
                first = false;
            }

            try {
                queryBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.name()))
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.name()));
            } catch (UnsupportedEncodingException e) {
                // UTF-8 should always be supported
                throw new RuntimeException("UTF-8 encoding not supported", e);
            }
        }

        return queryBuilder.toString();
    }
}
