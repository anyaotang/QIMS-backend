package com.qims.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qims.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 检测任务实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("qims_inspection_task")
public class InspectionTask extends BaseEntity {

    /** 任务名称 */
    private String name;

    /** 任务编码 */
    private String code;

    /** 关联节点ID */
    private Long nodeId;

    /** 任务状态：0=待检测 1=检测中 2=已完成 3=已取消 */
    private Integer status;

    /** 检测项总数 */
    private Integer totalItems;

    /** 已完成检测项数 */
    private Integer completedItems;

    /** 合格数 */
    private Integer qualifiedCount;

    /** 不合格数 */
    private Integer unqualifiedCount;

    /** 计划开始时间 */
    private LocalDateTime planStartTime;

    /** 计划结束时间 */
    private LocalDateTime planEndTime;

    /** 实际开始时间 */
    private LocalDateTime actualStartTime;

    /** 实际结束时间 */
    private LocalDateTime actualEndTime;

    /** 检测人员 */
    private String inspector;

    /** 备注 */
    private String remark;
}
