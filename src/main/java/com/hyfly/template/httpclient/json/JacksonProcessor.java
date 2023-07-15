package com.hyfly.template.httpclient.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * Jackson JSON处理器实现
 */
@Slf4j
public class JacksonProcessor implements JsonProcessor {

    private final ObjectMapper objectMapper;

    public JacksonProcessor() {
        this.objectMapper = new ObjectMapper();
    }

    public JacksonProcessor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String toJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("Jackson序列化失败", e);
            throw new RuntimeException("JSON序列化失败", e);
        }
    }

    @Override
    public <T> T parseObject(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            log.error("Jackson反序列化失败: {}", json, e);
            throw new RuntimeException("JSON反序列化失败", e);
        }
    }

    @Override
    public String getProcessorName() {
        return "Jackson";
    }
}
