package com.qims.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qims.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 检测记录实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("qims_inspection_record")
public class InspectionRecord extends BaseEntity {

    /** 检测项ID */
    private Long itemId;

    /** 检测值 */
    private BigDecimal value;

    /** 是否达标：1达标 0不达标 */
    private Integer isQualified;

    /** 检测时间 */
    private java.time.LocalDateTime inspectTime;

    /** 数据来源：0-手动 1-API 2-公式 3-定时采集 */
    private Integer dataSource;

    /** 备注 */
    private String remark;
}
