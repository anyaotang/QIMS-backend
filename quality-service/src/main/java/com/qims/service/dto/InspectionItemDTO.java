package com.qims.service.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.IOException;

/**
 * 新增/更新检测项 DTO
 */
@Data
@Schema(description = "新增/更新检测项请求")
public class InspectionItemDTO {

    @Schema(description = "检测项ID（新增时不传，更新时必传）")
    private Long id;

    @Schema(description = "检测项名称", example = "温度检测", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "检测项名称不能为空")
    private String name;

    @Schema(description = "关联节点ID")
    private Long nodeId;

    @Schema(description = "数据来源：0-手动 1-API采集 2-公式计算", example = "0")
    private Integer dataSource;

    @Schema(description = "目标值", example = "100")
    private String targetValue;

    @Schema(description = "单位", example = "℃")
    private String unit;

    @Schema(description = "是否启用：true-启用 false-禁用")
    @JsonDeserialize(using = BooleanToIntegerDeserializer.class)
    private Integer isActive;

    @Schema(description = "Cron 表达式（定时采集时使用）", example = "0 0/30 * * * ?")
    private String cronExpression;

    @Schema(description = "API 地址（dataSource=1 时使用）")
    private String apiUrl;

    @Schema(description = "公式表达式（dataSource=2 时使用）")
    private String formula;

    @Schema(description = "推送条件（Aviator 表达式）")
    private String pushCondition;

    @Schema(description = "推送邮箱（逗号分隔）")
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
