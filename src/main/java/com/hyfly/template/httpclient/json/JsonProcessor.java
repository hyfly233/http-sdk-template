package com.hyfly.template.httpclient.json;

/**
 * JSON处理器接口
 * 支持不同的JSON库实现
 */
public interface JsonProcessor {

    /**
     * 对象序列化为JSON字符串
     *
     * @param obj 要序列化的对象
     * @return JSON字符串
     */
    String toJsonString(Object obj);

    /**
     * JSON字符串反序列化为对象
     *
     * @param json JSON字符串
     * @param clazz 目标类型
     * @param <T> 泛型类型
     * @return 反序列化后的对象
     */
    <T> T parseObject(String json, Class<T> clazz);

    /**
     * 获取处理器名称
     *
     * @return 处理器名称
     */
    String getProcessorName();
}
