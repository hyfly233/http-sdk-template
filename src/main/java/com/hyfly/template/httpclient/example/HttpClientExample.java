package com.hyfly.template.httpclient.example;

import com.hyfly.template.httpclient.HttpClient;
import com.hyfly.template.httpclient.factory.HttpClientFactory;
import com.hyfly.template.httpclient.model.Header;
import com.hyfly.template.httpclient.model.HttpRestResult;
import com.hyfly.template.httpclient.model.Query;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP客户端模板使用示例
 */
@Slf4j
public class HttpClientExample {

    public static void main(String[] args) {
        // 示例1: 简单的REST API调用
        simpleApiExample();

        // 示例2: 带认证的API调用
        authenticatedApiExample();

        // 示例3: 文件上传示例
        // fileUploadExample();

        // 示例4: 自定义配置示例
        customConfigExample();
    }

    /**
     * 简单的REST API调用示例
     */
    public static void simpleApiExample() {
        log.info("=== 简单的REST API调用示例 ===");

        // 创建HTTP客户端
        HttpClient client = new HttpClient("https://jsonplaceholder.typicode.com");

        // GET请求获取用户信息
        HttpRestResult<String> getUserResult = client.get("/users/1", String.class);
        if (getUserResult.isSuccess()) {
            log.info("获取用户信息成功: {}", getUserResult.getData());
        } else {
            log.error("获取用户信息失败: {}", getUserResult.getMessage());
        }

        // POST请求创建新用户
        UserCreateRequest newUser = new UserCreateRequest();
        newUser.setName("张三");
        newUser.setUsername("zhangsan");
        newUser.setEmail("zhangsan@example.com");

        HttpRestResult<String> createUserResult = client.postJson("/users", newUser, String.class);
        if (createUserResult.isSuccess()) {
            log.info("创建用户成功: {}", createUserResult.getData());
        } else {
            log.error("创建用户失败: {}", createUserResult.getMessage());
        }
    }

    /**
     * 带认证的API调用示例
     */
    public static void authenticatedApiExample() {
        log.info("=== 带认证的API调用示例 ===");

        HttpClient client = new HttpClient("https://api.github.com");

        // 设置认证信息
        Header header = Header.newInstance()
                .setAuthorization("token your-github-token")
                .setUserAgent("HttpClientTemplate/1.0")
                .addParam("Accept", "application/vnd.github.v3+json");

        // 获取认证用户信息
        HttpRestResult<String> userResult = client.get("/user", header, new Query(), String.class);
        if (userResult.isSuccess()) {
            log.info("获取GitHub用户信息成功: {}", userResult.getData());
        } else {
            log.error("获取GitHub用户信息失败: {}", userResult.getMessage());
        }

        // 查询参数示例
        Query query = new Query()
                .addParam("type", "owner")
                .addParam("sort", "updated")
                .addParam("per_page", 5);

        HttpRestResult<String> reposResult = client.get("/user/repos", header, query, String.class);
        if (reposResult.isSuccess()) {
            log.info("获取用户仓库成功: {}", reposResult.getData());
        } else {
            log.error("获取用户仓库失败: {}", reposResult.getMessage());
        }
    }

    /**
     * 表单数据提交示例
     */
    public static void formDataExample() {
        log.info("=== 表单数据提交示例 ===");

        HttpClient client = new HttpClient("https://httpbin.org");

        // 准备表单数据
        Map<String, Object> formData = new HashMap<>();
        formData.put("name", "张三");
        formData.put("email", "zhangsan@example.com");
        formData.put("age", 25);
        formData.put("city", "北京");

        // 提交表单数据
        HttpRestResult<String> result = client.postForm("/post", formData, String.class);
        if (result.isSuccess()) {
            log.info("表单提交成功: {}", result.getData());
        } else {
            log.error("表单提交失败: {}", result.getMessage());
        }
    }

    /**
     * 自定义配置示例
     */
    public static void customConfigExample() {
        log.info("=== 自定义配置示例 ===");

        // 创建自定义配置的HTTP客户端
        var customRestTemplate = HttpClientFactory.getCustomApacheRestTemplate(
                5000,   // 连接超时 5秒
                30000,  // Socket超时 30秒
                10000   // 请求超时 10秒
        );

        HttpClient client = new HttpClient("https://httpbin.org", customRestTemplate);

        // 自定义请求头
        Header header = Header.newInstance()
                .setUserAgent("CustomHttpClient/1.0")
                .addParam("X-API-Version", "v1")
                .addParam("X-Request-ID", "req-" + System.currentTimeMillis());

        // 发送请求
        HttpRestResult<String> result = client.get("/get", header, new Query(), String.class);
        if (result.isSuccess()) {
            log.info("自定义配置请求成功: {}", result.getData());
        } else {
            log.error("自定义配置请求失败: {}", result.getMessage());
        }
    }

    /**
     * 错误处理示例
     */
    public static void errorHandlingExample() {
        log.info("=== 错误处理示例 ===");

        HttpClient client = new HttpClient("https://httpbin.org");

        // 模拟404错误
        HttpRestResult<String> notFoundResult = client.get("/status/404", String.class);
        handleResult(notFoundResult, "404错误测试");

        // 模拟500错误
        HttpRestResult<String> serverErrorResult = client.get("/status/500", String.class);
        handleResult(serverErrorResult, "500错误测试");

        // 模拟网络错误（无效URL）
        HttpClient invalidClient = new HttpClient("https://invalid-url-that-does-not-exist.com");
        HttpRestResult<String> networkErrorResult = invalidClient.get("/test", String.class);
        handleResult(networkErrorResult, "网络错误测试");
    }

    /**
     * 统一处理请求结果
     */
    private static void handleResult(HttpRestResult<String> result, String operation) {
        if (result.isSuccess()) {
            log.info("{} - 成功: {}", operation, result.getData());
        } else {
            log.error("{} - 失败 [{}]: {}", operation, result.getCode(), result.getMessage());
        }
    }

    /**
     * 用户创建请求DTO
     */
    @Data
    public static class UserCreateRequest {
        private String name;
        private String username;
        private String email;
    }
}
