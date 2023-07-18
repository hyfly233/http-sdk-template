package com.hyfly.template.httpclient.model;

import com.google.common.base.Strings;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * HTTP请求头封装类
 */
@Getter
public class Header {

    private static final String DEFAULT_CHARSET = "UTF-8";

    private final Map<String, String> header;

    private Header() {
        header = new LinkedHashMap<>();
        addParam(HttpHeaders.ACCEPT_CHARSET, DEFAULT_CHARSET);
        addParam(HttpHeaders.ACCEPT, MediaType.JSON_UTF_8.toString());
        addParam(HttpHeaders.CONTENT_TYPE, MediaType.FORM_DATA.toString());
    }

    public static Header newInstance() {
        return new Header();
    }

    /**
     * 添加请求头参数
     *
     * @param key   参数名
     * @param value 参数值
     * @return Header
     */
    public Header addParam(String key, String value) {
        if (!Strings.isNullOrEmpty(key)) {
            header.put(key, value);
        }
        return this;
    }

    /**
     * 设置Content-Type
     *
     * @param contentType 内容类型
     * @return Header
     */
    public Header setContentType(String contentType) {
        if (contentType == null) {
            contentType = MediaType.FORM_DATA.toString();
        }
        return addParam(HttpHeaders.CONTENT_TYPE, contentType);
    }

    /**
     * 设置Authorization
     *
     * @param token 认证令牌
     * @return Header
     */
    public Header setAuthorization(String token) {
        if (!Strings.isNullOrEmpty(token)) {
            addParam(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        }
        return this;
    }

    /**
     * 设置User-Agent
     *
     * @param userAgent 用户代理
     * @return Header
     */
    public Header setUserAgent(String userAgent) {
        if (!Strings.isNullOrEmpty(userAgent)) {
            addParam(HttpHeaders.USER_AGENT, userAgent);
        }
        return this;
    }

    public Header build() {
        return this;
    }

    /**
     * 获取请求头值
     *
     * @param key 请求头名称
     * @return 请求头值
     */
    public String getValue(String key) {
        return header.get(key);
    }

    /**
     * 获取字符集
     *
     * @return 字符集
     */
    public String getCharset() {
        String charset = getValue(HttpHeaders.ACCEPT_CHARSET);
        return charset != null ? charset : StandardCharsets.UTF_8.name();
    }

    /**
     * 判断是否为空
     *
     * @return 是否为空
     */
    public boolean isEmpty() {
        return header.isEmpty();
    }

    /**
     * 获取迭代器
     *
     * @return 迭代器
     */
    public Iterator<Map.Entry<String, String>> iterator() {
        return header.entrySet().iterator();
    }
}
