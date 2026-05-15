package com.qims.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.math.BigInteger;

/**
 * Jackson 配置
 * 解决 JavaScript 长整型（Long/BigInteger）精度丢失问题
 * <p>
 * 问题原因：
 * JavaScript 的 Number 类型最大安全整数为 2^53-1 (9007199254740991)
 * 而雪花ID（Snowflake ID）通常为 18-19 位数字，超出此范围会导致精度丢失
 * <p>
 * 解决方案：
 * 将 Java Long 和 BigInteger 类型序列化为字符串，前端接收后保持原值不变
 */
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();

        // 创建自定义模块，将 Long 和 BigInteger 序列化为字符串
        SimpleModule module = new SimpleModule();
        module.addSerializer(Long.class, ToStringSerializer.instance);
        module.addSerializer(Long.TYPE, ToStringSerializer.instance);
        module.addSerializer(BigInteger.class, ToStringSerializer.instance);

        objectMapper.registerModule(module);
        return objectMapper;
    }
}
