package com.qims.service.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 手动录入检测记录 DTO
 */
@Data
public class RecordManualDTO {

    private Long itemId;

    private BigDecimal value;

    private String remark;
}
