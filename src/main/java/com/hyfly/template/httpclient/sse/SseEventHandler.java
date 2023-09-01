package com.hyfly.template.httpclient.sse;

/**
 * SSE事件处理器接口
 */
public interface SseEventHandler {

    /**
     * 处理SSE事件
     *
     * @param event SSE事件
     */
    void onEvent(SseEvent event);

    /**
     * 连接打开时调用
     */
    default void onOpen() {
        // 默认实现为空
    }

    /**
     * 连接关闭时调用
     */
    default void onClose() {
        // 默认实现为空
    }

    /**
     * 发生错误时调用
     *
     * @param throwable 异常
     */
    default void onError(Throwable throwable) {
        // 默认实现为空
    }

    /**
     * 处理心跳事件
     *
     * @param event 心跳事件
     */
    default void onHeartbeat(SseEvent event) {
        // 默认实现为空
    }
}
