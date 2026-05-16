package com.qims.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 报表查询条件 DTO
 */
@Data
@Schema(description = "报表查询条件")
public class ReportQueryDTO {

    @Schema(description = "节点ID")
    private Long nodeId;

    @Schema(description = "检测项ID")
    private Long itemId;

    @Schema(description = "是否达标：1-达标 0-不达标")
    private Integer isQualified;

    @Schema(description = "开始日期", example = "2024-01-01")
    private String startDate;

    @Schema(description = "结束日期", example = "2024-12-31")
    private String endDate;

    @Schema(description = "页码", example = "1")
    private Integer page = 1;

    @Schema(description = "每页条数", example = "10")
    private Integer pageSize = 10;
}
