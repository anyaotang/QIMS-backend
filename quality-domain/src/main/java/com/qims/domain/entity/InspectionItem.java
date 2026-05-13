package com.qims.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qims.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 检测项实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("qims_inspection_item")
public class InspectionItem extends BaseEntity {

    /** 检测项名称 */
    private String name;

    /** 关联节点ID */
    private Long nodeId;

    /** 数据来源：0-手动 1-API采集 2-公式计算 */
    private Integer dataSource;

    /** 目标值 */
    private String targetValue;

    /** 单位 */
    private String unit;

    /** 是否启用：1启用 0禁用 */
    private Integer isActive;

    /** Cron 表达式（定时采集时使用） */
    private String cronExpression;

    /** API 地址（dataSource=1 时使用） */
    private String apiUrl;

    /** 公式表达式（dataSource=2 时使用） */
    private String formula;

    /** 推送条件（Aviator 表达式） */
    private String pushCondition;

    /** 推送邮箱（逗号分隔） */
    private String pushEmails;
}
