package com.hyfly.template.httpclient.sse;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * SSE事件解析器 解析SSE流并转换为SseEvent对象
 */
@Slf4j
public class SseEventParser {

    private final AtomicBoolean running = new AtomicBoolean(false);

    /**
     * 解析SSE流
     *
     * @param inputStream SSE输入流
     * @param handler     事件处理器
     */
    public void parseStream(InputStream inputStream, SseEventHandler handler) {
        if (!running.compareAndSet(false, true)) {
            throw new IllegalStateException("解析器已在运行中");
        }

        try {
            handler.onOpen();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            SseEvent.Builder eventBuilder = new SseEvent.Builder();
            String line;

            while (running.get() && (line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    // 空行表示事件结束
                    SseEvent event = eventBuilder.build();
                    if (event != null) {
                        processEvent(event, handler);
                        eventBuilder = new SseEvent.Builder();
                    }
                } else {
                    parseLine(line, eventBuilder);
                }
            }

            // 处理最后一个事件（如果没有以空行结尾）
            SseEvent lastEvent = eventBuilder.build();
            if (lastEvent != null) {
                processEvent(lastEvent, handler);
            }

        } catch (IOException e) {
            log.error("SSE流解析异常", e);
            handler.onError(e);
        } finally {
            running.set(false);
            handler.onClose();
        }
    }

    /**
     * 解析单行SSE数据
     */
    private void parseLine(String line, SseEvent.Builder builder) {
        int colonIndex = line.indexOf(':');

        if (colonIndex == -1) {
            // 没有冒号，整行作为字段名，值为空
            builder.setField(line.trim(), "");
        } else {
            String field = line.substring(0, colonIndex).trim();
            String value = line.substring(colonIndex + 1).trim();

            // 跳过以 '#' 开头的注释行
            if (field.startsWith("#")) {
                return;
            }

            builder.setField(field, value);
        }
    }

    /**
     * 处理解析出的事件
     */
    private void processEvent(SseEvent event, SseEventHandler handler) {
        try {
            if (event.isHeartbeat()) {
                handler.onHeartbeat(event);
            } else {
                handler.onEvent(event);
            }

            // 检查是否是结束事件
            if (event.isEnd()) {
                stop();
            }
        } catch (Exception e) {
            log.error("处理SSE事件异常: {}", event, e);
            handler.onError(e);
        }
    }

    /**
     * 停止解析
     */
    public void stop() {
        running.set(false);
    }

    /**
     * 判断是否正在运行
     */
    public boolean isRunning() {
        return running.get();
    }
}
