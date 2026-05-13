package com.qims.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qims.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 实施方案反馈实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("qims_implementation_feedback")
public class ImplementationFeedback extends BaseEntity {

    /** 方案ID */
    private Long planId;

    /** 反馈人 */
    private String feedbackBy;

    /** 反馈内容 */
    private String content;
}
