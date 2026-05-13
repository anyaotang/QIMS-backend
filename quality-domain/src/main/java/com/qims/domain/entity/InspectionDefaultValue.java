package com.qims.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qims.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 检测项默认值实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("qims_inspection_default_value")
public class InspectionDefaultValue extends BaseEntity {

    /** 检测项ID */
    private Long itemId;

    /** 分组名称 */
    private String groupName;

    /** 默认值 */
    private String defaultValue;
}
