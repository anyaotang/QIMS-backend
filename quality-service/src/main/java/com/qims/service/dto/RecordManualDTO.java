package com.qims.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 手动录入检测记录 DTO
 */
@Data
@Schema(description = "手动录入检测记录请求")
public class RecordManualDTO {

    @Schema(description = "检测项ID", example = "1")
    private Long itemId;

    @Schema(description = "检测值", example = "99.5")
    private BigDecimal value;

    @Schema(description = "备注")
    private String remark;
}
