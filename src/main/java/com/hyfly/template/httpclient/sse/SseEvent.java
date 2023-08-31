package com.hyfly.template.httpclient.sse;

import lombok.Data;

/**
 * SSE事件数据模型
 */
@Data
public class SseEvent {

    /**
     * 事件ID
     */
    private String id;

    /**
     * 事件类型
     */
    private String event;

    /**
     * 事件数据
     */
    private String data;

    /**
     * 重连间隔（毫秒）
     */
    private Long retry;

    /**
     * 原始行数据
     */
    private String rawData;

    public SseEvent() {}

    public SseEvent(String data) {
        this.data = data;
    }

    public SseEvent(String event, String data) {
        this.event = event;
        this.data = data;
    }

    /**
     * 判断是否是心跳事件
     */
    public boolean isHeartbeat() {
        return "ping".equals(event) || "heartbeat".equals(event);
    }

    /**
     * 判断是否是结束事件
     */
    public boolean isEnd() {
        return "end".equals(event) || "close".equals(event) || "[DONE]".equals(data);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (id != null) {
            sb.append("id: ").append(id).append("\n");
        }
        if (event != null) {
            sb.append("event: ").append(event).append("\n");
        }
        if (data != null) {
            sb.append("data: ").append(data).append("\n");
        }
        if (retry != null) {
            sb.append("retry: ").append(retry).append("\n");
        }
        sb.append("\n");
        return sb.toString();
    }
}
