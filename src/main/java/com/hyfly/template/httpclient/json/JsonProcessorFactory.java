package com.hyfly.template.httpclient.json;

/**
 * JSON处理器工厂
 */
public class JsonProcessorFactory {

    public static final String FASTJSON2 = "fastjson2";
    public static final String JACKSON = "jackson";

    private static JsonProcessor defaultProcessor = new Fastjson2Processor();

    /**
     * 获取默认的JSON处理器
     *
     * @return JSON处理器
     */
    public static JsonProcessor getDefaultProcessor() {
        return defaultProcessor;
    }

    /**
     * 设置默认的JSON处理器
     *
     * @param processor JSON处理器
     */
    public static void setDefaultProcessor(JsonProcessor processor) {
        defaultProcessor = processor;
    }

    /**
     * 根据类型获取JSON处理器
     *
     * @param type 处理器类型
     * @return JSON处理器
     */
    public static JsonProcessor getProcessor(String type) {
        switch (type.toLowerCase()) {
            case FASTJSON2:
                return new Fastjson2Processor();
            case JACKSON:
                return new JacksonProcessor();
            default:
                throw new IllegalArgumentException("不支持的JSON处理器类型: " + type);
        }
    }

    /**
     * 获取Fastjson2处理器
     *
     * @return Fastjson2处理器
     */
    public static JsonProcessor getFastjson2Processor() {
        return new Fastjson2Processor();
    }

    /**
     * 获取Jackson处理器
     *
     * @return Jackson处理器
     */
    public static JsonProcessor getJacksonProcessor() {
        return new JacksonProcessor();
    }
}
