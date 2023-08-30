package com.hyfly.template.httpclient.sse;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * SSE客户端
 * 提供SSE连接管理和事件处理功能
 */
@Slf4j
public class SseClient {

    private final Executor executor;
    private final SseEventParser parser;

    public SseClient() {
        this.executor = Executors.newCachedThreadPool(r -> {
            Thread thread = new Thread(r, "SSE-Worker");
            thread.setDaemon(true);
            return thread;
        });
        this.parser = new SseEventParser();
    }

    public SseClient(Executor executor) {
        this.executor = executor;
        this.parser = new SseEventParser();
    }

    /**
     * 异步处理SSE响应
     *
     * @param response HTTP响应对象
     * @param handler  事件处理器
     * @return CompletableFuture，可用于管理连接生命周期
     */
    public CompletableFuture<Void> handleSseResponse(HttpClientResponse response, SseEventHandler handler) {
        return CompletableFuture.runAsync(() -> {
            try {
                if (!"text/event-stream".equals(response.getContentType()) &&
                    !"text/plain".equals(response.getContentType())) {
                    log.warn("响应Content-Type不是标准SSE格式: {}", response.getContentType());
                }

                parser.parseStream(response.getBody(), handler);
            } catch (Exception e) {
                log.error("SSE响应处理异常", e);
                handler.onError(e);
            }
        }, executor);
    }

    /**
     * 同步处理SSE响应
     *
     * @param response HTTP响应对象
     * @param handler  事件处理器
     */
    public void handleSseResponseSync(HttpClientResponse response, SseEventHandler handler) {
        try {
            if (!"text/event-stream".equals(response.getContentType()) &&
                !"text/plain".equals(response.getContentType())) {
                log.warn("响应Content-Type不是标准SSE格式: {}", response.getContentType());
            }

            parser.parseStream(response.getBody(), handler);
        } catch (Exception e) {
            log.error("SSE响应处理异常", e);
            handler.onError(e);
        }
    }

    /**
     * 停止SSE解析
     */
    public void stop() {
        parser.stop();
    }

    /**
     * 判断是否正在运行
     */
    public boolean isRunning() {
        return parser.isRunning();
    }

    /**
     * 关闭SSE客户端
     */
    public void close() {
        stop();
        if (executor instanceof AutoCloseable) {
            try {
                ((AutoCloseable) executor).close();
            } catch (Exception e) {
                log.warn("关闭SSE客户端线程池异常", e);
            }
        }
    }
}
