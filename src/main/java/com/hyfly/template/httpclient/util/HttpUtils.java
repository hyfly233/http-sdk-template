package com.hyfly.template.httpclient.util;

import com.template.httpclient.model.Query;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * HTTP工具类
 */
public class HttpUtils {

    /**
     * 根据URL和查询参数构建URI
     *
     * @param url   URL
     * @param query 查询参数
     * @return URI
     * @throws URISyntaxException URI语法异常
     */
    public static URI buildUri(String url, Query query) throws URISyntaxException {
        if (query != null && !query.isEmpty()) {
            url = url + "?" + query.toQueryUrl();
        }
        return new URI(url);
    }

    /**
     * 判断URL是否为HTTPS
     *
     * @param url URL
     * @return 是否为HTTPS
     */
    public static boolean isHttps(String url) {
        return url != null && url.toLowerCase().startsWith("https://");
    }

    /**
     * 拼接URL路径
     *
     * @param baseUrl 基础URL
     * @param path    路径
     * @return 完整URL
     */
    public static String joinPath(String baseUrl, String path) {
        if (baseUrl == null) {
            return path;
        }
        if (path == null) {
            return baseUrl;
        }

        String normalizedBase = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        String normalizedPath = path.startsWith("/") ? path : "/" + path;

        return normalizedBase + normalizedPath;
    }
}
