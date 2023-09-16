package com.hyfly.template.httpclient.example;

import com.hyfly.template.httpclient.HttpClient;
import com.hyfly.template.httpclient.response.HttpClientResponse;
import com.hyfly.template.httpclient.sse.SseClient;
import com.hyfly.template.httpclient.sse.SseEvent;
import com.hyfly.template.httpclient.sse.SseEventHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * SSE使用示例 演示如何使用HttpClient处理Server-Sent Events
 */
@Slf4j
public class SseExample {

    private final HttpClient httpClient;
    private final SseClient sseClient;

    public SseExample() {
        this.httpClient = new HttpClient("https://api.example.com");
        this.sseClient = new SseClient();
    }

    /**
     * 演示所有SSE示例
     */
    public static void main(String[] args) {
        SseExample example = new SseExample();

        try {
            // 演示ChatGPT风格流式对话
            example.chatGptStyleStream();

            Thread.sleep(2000);

            // 演示实时日志流
            example.realTimeLogStream();

            Thread.sleep(2000);

            // 演示进度监控
            example.progressMonitorStream();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            // 清理资源
            example.sseClient.close();
        }
    }

    /**
     * ChatGPT风格的流式对话示例
     */
    public void chatGptStyleStream() {
        log.info("=== ChatGPT风格流式对话示例 ===");

        try {
            // 模拟ChatGPT API调用
            String url = "https://api.openai.com/v1/chat/completions";

            HttpClientResponse response = httpClient.getRestTemplate().sseStream(url);

            CountDownLatch latch = new CountDownLatch(1);
            StringBuilder fullResponse = new StringBuilder();

            // 创建SSE事件处理器
            SseEventHandler handler =
                    new SseEventHandler() {
                        @Override
                        public void onOpen() {
                            log.info("SSE连接已建立");
                        }

                        @Override
                        public void onEvent(SseEvent event) {
                            log.debug("收到SSE事件: {}", event.getEvent());

                            if ("data".equals(event.getEvent())) {
                                String data = event.getData();
                                if ("[DONE]".equals(data)) {
                                    log.info("流式响应完成");
                                    latch.countDown();
                                    return;
                                }

                                // 解析JSON数据并提取内容
                                try {
                                    // 这里可以使用JSON处理器解析响应
                                    String content = extractContentFromChatResponse(data);
                                    if (content != null && !content.isEmpty()) {
                                        fullResponse.append(content);
                                        System.out.print(content); // 实时打印
                                    }
                                } catch (Exception e) {
                                    log.error("解析响应数据失败: {}", data, e);
                                }
                            }
                        }

                        @Override
                        public void onClose() {
                            log.info("SSE连接已关闭");
                            log.info("完整响应: {}", fullResponse);
                            latch.countDown();
                        }

                        @Override
                        public void onError(Throwable error) {
                            log.error("SSE连接异常", error);
                            latch.countDown();
                        }

                        @Override
                        public void onHeartbeat(SseEvent event) {
                            log.debug("收到心跳: {}", event);
                        }
                    };

            // 异步处理SSE响应
            CompletableFuture<Void> future = sseClient.handleSseResponse(response, handler);

            // 等待完成或超时
            boolean completed = latch.await(30, TimeUnit.SECONDS);
            if (!completed) {
                log.warn("SSE响应超时");
                sseClient.stop();
            }

        } catch (Exception e) {
            log.error("ChatGPT流式对话异常", e);
        }
    }

    /**
     * 实时日志流示例
     */
    public void realTimeLogStream() {
        log.info("=== 实时日志流示例 ===");

        try {
            String url = "http://localhost:8080/api/logs/stream";

            HttpClientResponse response = httpClient.getRestTemplate().sseStream(url);

            CountDownLatch latch = new CountDownLatch(1);

            SseEventHandler handler =
                    new SseEventHandler() {
                        @Override
                        public void onOpen() {
                            log.info("开始接收实时日志");
                        }

                        @Override
                        public void onEvent(SseEvent event) {
                            String logLevel = event.getEvent(); // 日志级别
                            String logMessage = event.getData(); // 日志内容

                            // 根据日志级别处理
                            switch (logLevel != null ? logLevel : "INFO") {
                                case "ERROR":
                                    log.error("[实时日志] {}", logMessage);
                                    break;
                                case "WARN":
                                    log.warn("[实时日志] {}", logMessage);
                                    break;
                                case "DEBUG":
                                    log.debug("[实时日志] {}", logMessage);
                                    break;
                                default:
                                    log.info("[实时日志] {}", logMessage);
                                    break;
                            }
                        }

                        @Override
                        public void onClose() {
                            log.info("实时日志流已结束");
                            latch.countDown();
                        }

                        @Override
                        public void onError(Throwable error) {
                            log.error("实时日志流异常", error);
                            latch.countDown();
                        }

                        @Override
                        public void onHeartbeat(SseEvent event) {
                            // 心跳保活
                        }
                    };

            // 同步处理（也可以异步）
            sseClient.handleSseResponseSync(response, handler);

        } catch (Exception e) {
            log.error("实时日志流异常", e);
        }
    }

    /**
     * 从ChatGPT响应中提取内容
     */
    private String extractContentFromChatResponse(String data) {
        // 简化的JSON解析示例
        // 实际项目中应使用完整的JSON处理器
        if (data.contains("\"content\":")) {
            int start = data.indexOf("\"content\":\"") + 11;
            int end = data.indexOf("\"", start);
            if (end > start) {
                return data.substring(start, end).replace("\\n", "\n");
            }
        }
        return null;
    }

    /**
     * 更新进度条显示
     */
    private void updateProgressBar(int progress) {
        int barLength = 50;
        int completed = (int) (progress * barLength / 100.0);

        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < barLength; i++) {
            if (i < completed) {
                bar.append("=");
            } else if (i == completed) {
                bar.append(">");
            } else {
                bar.append(" ");
            }
        }
        bar.append("]");

        System.out.printf("\r%s %d%%", bar, progress);
        if (progress >= 100) {
            System.out.println(); // 换行
        }
    }

    /**
     * 进度监控示例
     */
    public void progressMonitorStream() {
        log.info("=== 进度监控示例 ===");

        try {
            String url = "http://localhost:8080/api/tasks/123/progress";

            HttpClientResponse response = httpClient.getRestTemplate().sseStream(url);

            SseEventHandler handler =
                    new SseEventHandler() {
                        @Override
                        public void onOpen() {
                            log.info("开始监控任务进度");
                        }

                        @Override
                        public void onEvent(SseEvent event) {
                            String eventType = event.getEvent();
                            String data = event.getData();

                            switch (eventType != null ? eventType : "progress") {
                                case "progress":
                                    // 解析进度数据
                                    int progress = Integer.parseInt(data);
                                    log.info("任务进度: {}%", progress);
                                    updateProgressBar(progress);
                                    break;
                                case "status":
                                    log.info("任务状态变更: {}", data);
                                    break;
                                case "complete":
                                    log.info("任务完成: {}", data);
                                    break;
                                case "error":
                                    log.error("任务异常: {}", data);
                                    break;
                            }
                        }

                        @Override
                        public void onClose() {
                            log.info("进度监控结束");
                        }

                        @Override
                        public void onError(Throwable error) {
                            log.error("进度监控异常", error);
                        }

                        @Override
                        public void onHeartbeat(SseEvent event) {
                            log.debug("连接正常");
                        }
                    };

            sseClient.handleSseResponseSync(response, handler);

        } catch (Exception e) {
            log.error("进度监控异常", e);
        }
    }
}
