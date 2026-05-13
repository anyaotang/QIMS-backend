package com.qims.service.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.IOException;

/**
 * 新增/更新检测项 DTO
 */
@Data
public class InspectionItemDTO {

    private Long id;

    @NotBlank(message = "检测项名称不能为空")
    private String name;

    private Long nodeId;

    /** 数据来源：0-手动 1-API采集 2-公式计算 */
    private Integer dataSource;

    private String targetValue;
    private String unit;

    /** 前端传 true/false，Jackson 反序列化为 1/0 */
    @JsonDeserialize(using = BooleanToIntegerDeserializer.class)
    private Integer isActive;

    private String cronExpression;
    private String apiUrl;
    private String formula;
    private String pushCondition;
    private String pushEmails;

    /**
     * Jackson 反序列化器：将 true/false 转为 1/0
     */
    public static class BooleanToIntegerDeserializer extends JsonDeserializer<Integer> {
        @Override
        public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return Boolean.TRUE.equals(p.getBooleanValue()) ? 1 : 0;
        }
    }
}
