package com.qims.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 实施方案反馈 DTO
 */
@Data
@Schema(description = "实施方案反馈请求")
public class FeedbackDTO {

    @Schema(description = "方案ID", example = "1")
    private Long planId;

    @Schema(description = "反馈内容")
    private String content;
}
