package com.qims.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qims.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 检测任务项实体（任务与检测项的关联 + 检测结果）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("qims_inspection_task_item")
public class InspectionTaskItem extends BaseEntity {

    /** 任务ID */
    private Long taskId;

    /** 检测项ID */
    private Long itemId;

    /** 检测项名称（冗余） */
    private String itemName;

    /** 检测项编码（冗余） */
    private String itemCode;

    /** 目标值 */
    private BigDecimal targetValue;

    /** 单位 */
    private String unit;

    /** 上限 */
    private BigDecimal upperLimit;

    /** 下限 */
    private BigDecimal lowerLimit;

    /** 实际检测值 */
    private BigDecimal actualValue;

    /** 偏差 */
    private BigDecimal deviation;

    /** 是否合格：1合格 0不合格 null=未检测 */
    private Integer isQualified;

    /** 检测人员 */
    private String inspector;

    /** 检测时间 */
    private java.time.LocalDateTime inspectTime;

    /** 检测备注 */
    private String remark;
}
