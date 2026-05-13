package com.qims.service.dto;

import lombok.Data;

/**
 * 报表查询条件 DTO
 */
@Data
public class ReportQueryDTO {

    private Long nodeId;
    private Long itemId;
    private Integer isQualified;
    private String startDate;
    private String endDate;
    private Integer page = 1;
    private Integer pageSize = 10;
}
