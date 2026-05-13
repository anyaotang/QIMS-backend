package com.qims.service.dto;

import lombok.Data;

/**
 * 实施方案反馈 DTO
 */
@Data
public class FeedbackDTO {

    private Long planId;
    private String content;
}
