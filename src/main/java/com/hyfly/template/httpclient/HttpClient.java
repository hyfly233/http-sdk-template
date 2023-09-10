package com.hyfly.template.httpclient;

import com.hyfly.template.httpclient.core.HttpRestTemplate;
import com.hyfly.template.httpclient.factory.HttpClientFactory;
import com.hyfly.template.httpclient.model.Header;
import com.hyfly.template.httpclient.model.HttpRestResult;
import com.hyfly.template.httpclient.model.Query;
import lombok.extern.slf4j.Slf4j;

/**
 * HTTP客户端入口类
 * 提供简化的HTTP请求操作接口
 */
@Slf4j
public class HttpClient {

    private final HttpRestTemplate restTemplate;
    private final String baseUrl;

    /**
     * 构造函数
     *
     * @param baseUrl 基础URL
     */
    public HttpClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.restTemplate = HttpClientFactory.getDefaultRestTemplate();
    }

    /**
     * 构造函数
     *
     * @param baseUrl      基础URL
     * @param restTemplate REST模板
     */
    public HttpClient(String baseUrl, HttpRestTemplate restTemplate) {
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
    }

    /**
     * GET请求
     *
     * @param path         请求路径
     * @param responseType 响应类型
     * @param <T>          响应数据类型
     * @return 响应结果
     */
    public <T> HttpRestResult<T> get(String path, Class<T> responseType) {
        return get(path, Header.newInstance(), new Query(), responseType);
    }

    /**
     * GET请求（带查询参数）
     *
     * @param path         请求路径
     * @param query        查询参数
     * @param responseType 响应类型
     * @param <T>          响应数据类型
     * @return 响应结果
     */
    public <T> HttpRestResult<T> get(String path, Query query, Class<T> responseType) {
        return get(path, Header.newInstance(), query, responseType);
    }

    /**
     * GET请求（完整参数）
     *
     * @param path         请求路径
     * @param header       请求头
     * @param query        查询参数
     * @param responseType 响应类型
     * @param <T>          响应数据类型
     * @return 响应结果
     */
    public <T> HttpRestResult<T> get(String path, Header header, Query query, Class<T> responseType) {
        try {
            String fullUrl = buildFullUrl(path);
            return restTemplate.get(fullUrl, header, query, responseType);
        } catch (Exception e) {
            log.error("GET请求失败: {}", path, e);
            return HttpRestResult.failure(500, "请求失败: " + e.getMessage());
        }
    }

    /**
     * POST JSON请求
     *
     * @param path         请求路径
     * @param body         请求体
     * @param responseType 响应类型
     * @param <T>          响应数据类型
     * @return 响应结果
     */
    public <T> HttpRestResult<T> postJson(String path, Object body, Class<T> responseType) {
        return postJson(path, Header.newInstance(), body, responseType);
    }

    /**
     * POST JSON请求（带请求头）
     *
     * @param path         请求路径
     * @param header       请求头
     * @param body         请求体
     * @param responseType 响应类型
     * @param <T>          响应数据类型
     * @return 响应结果
     */
    public <T> HttpRestResult<T> postJson(String path, Header header, Object body, Class<T> responseType) {
        try {
            String fullUrl = buildFullUrl(path);
            return restTemplate.postJson(fullUrl, header, body, responseType);
        } catch (Exception e) {
            log.error("POST JSON请求失败: {}", path, e);
            return HttpRestResult.failure(500, "请求失败: " + e.getMessage());
        }
    }

    /**
     * POST表单请求
     *
     * @param path         请求路径
     * @param body         请求体
     * @param responseType 响应类型
     * @param <T>          响应数据类型
     * @return 响应结果
     */
    public <T> HttpRestResult<T> postForm(String path, Object body, Class<T> responseType) {
        return postForm(path, Header.newInstance(), body, responseType);
    }

    /**
     * POST表单请求（带请求头）
     *
     * @param path         请求路径
     * @param header       请求头
     * @param body         请求体
     * @param responseType 响应类型
     * @param <T>          响应数据类型
     * @return 响应结果
     */
    public <T> HttpRestResult<T> postForm(String path, Header header, Object body, Class<T> responseType) {
        try {
            String fullUrl = buildFullUrl(path);
            return restTemplate.postForm(fullUrl, header, body, responseType);
        } catch (Exception e) {
            log.error("POST表单请求失败: {}", path, e);
            return HttpRestResult.failure(500, "请求失败: " + e.getMessage());
        }
    }

    /**
     * PUT JSON请求
     *
     * @param path         请求路径
     * @param body         请求体
     * @param responseType 响应类型
     * @param <T>          响应数据类型
     * @return 响应结果
     */
    public <T> HttpRestResult<T> putJson(String path, Object body, Class<T> responseType) {
        return putJson(path, Header.newInstance(), body, responseType);
    }

    /**
     * PUT JSON请求（带请求头）
     *
     * @param path         请求路径
     * @param header       请求头
     * @param body         请求体
     * @param responseType 响应类型
     * @param <T>          响应数据类型
     * @return 响应结果
     */
    public <T> HttpRestResult<T> putJson(String path, Header header, Object body, Class<T> responseType) {
        try {
            String fullUrl = buildFullUrl(path);
            return restTemplate.putJson(fullUrl, header, body, responseType);
        } catch (Exception e) {
            log.error("PUT JSON请求失败: {}", path, e);
            return HttpRestResult.failure(500, "请求失败: " + e.getMessage());
        }
    }

    /**
     * DELETE请求
     *
     * @param path         请求路径
     * @param responseType 响应类型
     * @param <T>          响应数据类型
     * @return 响应结果
     */
    public <T> HttpRestResult<T> delete(String path, Class<T> responseType) {
        return delete(path, Header.newInstance(), new Query(), responseType);
    }

    /**
     * DELETE请求（带查询参数）
     *
     * @param path         请求路径
     * @param query        查询参数
     * @param responseType 响应类型
     * @param <T>          响应数据类型
     * @return 响应结果
     */
    public <T> HttpRestResult<T> delete(String path, Query query, Class<T> responseType) {
        return delete(path, Header.newInstance(), query, responseType);
    }

    /**
     * DELETE请求（完整参数）
     *
     * @param path         请求路径
     * @param header       请求头
     * @param query        查询参数
     * @param responseType 响应类型
     * @param <T>          响应数据类型
     * @return 响应结果
     */
    public <T> HttpRestResult<T> delete(String path, Header header, Query query, Class<T> responseType) {
        try {
            String fullUrl = buildFullUrl(path);
            return restTemplate.delete(fullUrl, header, query, responseType);
        } catch (Exception e) {
            log.error("DELETE请求失败: {}", path, e);
            return HttpRestResult.failure(500, "请求失败: " + e.getMessage());
        }
    }

    /**
     * 构建完整URL
     *
     * @param path 请求路径
     * @return 完整URL
     */
    private String buildFullUrl(String path) {
        if (path == null) {
            return baseUrl;
        }
        if (path.startsWith("http://") || path.startsWith("https://")) {
            return path;
        }

        String normalizedBase = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        String normalizedPath = path.startsWith("/") ? path : "/" + path;

        return normalizedBase + normalizedPath;
    }
}
