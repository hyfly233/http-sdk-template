package com.hyfly.template.httpclient.model;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * HTTP请求实体类
 */
@Data
@Slf4j
public class RequestHttpEntity {

    private Header header;
    private Query query;
    private Object body;

    public RequestHttpEntity() {}

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

    /**
     * 当body是Map类型时转换为Map
     *
     * @return Map对象
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> castBodyToMap() {
        if (ifBodyIsMap()) {
            return (Map<String, Object>) body;
        }
        throw new UnsupportedOperationException("body不是Map类型，不能使用此方法");
    }

    /**
     * 判断body是否为Map类型
     *
     * @return 是否为Map类型
     */
    public boolean ifBodyIsMap() {
        return body instanceof Map;
    }

    /**
     * 将body转换为JSON字符串
     *
     * @return JSON字符串
     */
    public String bodyToJson() {
        if (Objects.isNull(body)) {
            return null;
        }
        return JSONObject.toJSONString(body);
    }

    /**
     * 通过反射将对象转换为Map
     *
     * @return Map对象
     */
    public Map<String, Object> bodyToMap() {
        if (Objects.isNull(body)) {
            return null;
        }

        Map<String, Object> map = new LinkedHashMap<>();
        Field[] declaredFields = body.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            try {
                Optional.ofNullable(field.get(body))
                        .ifPresent(value -> map.put(field.getName(), value));
            } catch (IllegalAccessException e) {
                log.error("对象转Map失败", e);
            }
        }
        return map;
    }
}
