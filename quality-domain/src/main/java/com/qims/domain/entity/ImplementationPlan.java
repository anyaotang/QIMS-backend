package com.qims.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qims.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 实施方案实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("qims_implementation_plan")
public class ImplementationPlan extends BaseEntity {

    /** 方案名称 */
    private String name;

    /** 父方案ID，0 为根节点 */
    private Long parentId;

    /** 责任人 */
    private String responsible;

    /** 截止日期 */
    private java.time.LocalDate deadline;

    /** 状态：0-未开始 1-进行中 2-已完成 */
    private Integer status;

    /** 描述 */
    private String description;
    /** 子方案列表（用于树形结构，非数据库字段） */
    @TableField(exist = false)
    private java.util.List<ImplementationPlan> children;
}
