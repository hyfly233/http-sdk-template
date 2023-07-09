package com.hyfly.template.httpclient.factory;

import com.google.common.base.Strings;
import com.hyfly.template.httpclient.core.HttpRestTemplate;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.RequestContent;

/**
 * HTTP客户端工厂类
 */
public class HttpClientFactory {

    public static final String HTTP_CLIENT_APACHE = "apache";
    public static final String HTTP_CLIENT_OKHTTP = "okhttp";

    /**
     * 获取REST模板
     *
     * @param type 客户端类型
     * @return HttpRestTemplate
     */
    public static HttpRestTemplate getRestTemplate(String type) {
        if (Strings.isNullOrEmpty(type)) {
            throw new IllegalArgumentException("客户端类型不能为空");
        }

        switch (type) {
            case HTTP_CLIENT_APACHE:
                return getApacheRestTemplate();
            case HTTP_CLIENT_OKHTTP:
                return getOkHttpRestTemplate();
            default:
                throw new UnsupportedOperationException("暂不支持的客户端类型: " + type);
        }
    }

    /**
     * 获取默认的REST模板（Apache HttpClient）
     *
     * @return HttpRestTemplate
     */
    public static HttpRestTemplate getDefaultRestTemplate() {
        return getApacheRestTemplate();
    }

    /**
     * 获取基于Apache HttpClient的REST模板
     *
     * @return HttpRestTemplate
     */
    public static HttpRestTemplate getApacheRestTemplate() {
        final RequestConfig defaultConfig = RequestConfig.custom()
                .setConnectTimeout(30000)
                .setSocketTimeout(30000)
                .setConnectionRequestTimeout(30000)
                .build();

        return new HttpRestTemplate(
                new ApacheHttpClientRequest(
                        HttpClients.custom()
                                .addInterceptorLast(new RequestContent(true))
                                .setDefaultRequestConfig(defaultConfig)
                                .build(),
                        defaultConfig));
    }

    /**
     * 获取基于OkHttp的REST模板
     *
     * @return HttpRestTemplate
     */
    public static HttpRestTemplate getOkHttpRestTemplate() {
        return new HttpRestTemplate(
                new com.hyfly.template.httpclient.request.OkHttpClientRequest(
                        30000,  // 连接超时
                        30000,  // 读取超时
                        30000   // 写入超时
                ));
    }

    /**
     * 获取自定义配置的Apache HttpClient REST模板
     *
     * @param connectTimeout    连接超时时间（毫秒）
     * @param socketTimeout     Socket超时时间（毫秒）
     * @param requestTimeout    请求超时时间（毫秒）
     * @return HttpRestTemplate
     */
    public static HttpRestTemplate getCustomApacheRestTemplate(int connectTimeout,
                                                               int socketTimeout,
                                                               int requestTimeout) {
        final RequestConfig defaultConfig = RequestConfig.custom()
                .setConnectTimeout(connectTimeout)
                .setSocketTimeout(socketTimeout)
                .setConnectionRequestTimeout(requestTimeout)
                .build();

        return new HttpRestTemplate(
                new ApacheHttpClientRequest(
                        HttpClients.custom()
                                .addInterceptorLast(new RequestContent(true))
                                .setDefaultRequestConfig(defaultConfig)
                                .build(),
                        defaultConfig));
    }
}
