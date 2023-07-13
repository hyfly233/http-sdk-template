package com.hyfly.template.httpclient.json;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;

/**
 * Fastjson2 JSON处理器实现
 */
@Slf4j
public class Fastjson2Processor implements JsonProcessor {

    @Override
    public String toJsonString(Object obj) {
        try {
            return JSON.toJSONString(obj);
        } catch (Exception e) {
            log.error("Fastjson2序列化失败", e);
            throw new RuntimeException("JSON序列化失败", e);
        }
    }

    @Override
    public <T> T parseObject(String json, Class<T> clazz) {
        try {
            return JSON.parseObject(json, clazz);
        } catch (Exception e) {
            log.error("Fastjson2反序列化失败: {}", json, e);
            throw new RuntimeException("JSON反序列化失败", e);
        }
    }

    @Override
    public String getProcessorName() {
        return "Fastjson2";
    }
}
