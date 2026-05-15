package com.qims.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qims.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 节点实体（部门/产品/工序/检测点）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("qims_node")
public class Node extends BaseEntity {

    /** 节点名称 */
    private String name;

    /** 父节点ID，0 为根节点 */
    private Long parentId;

    /** 节点类型：工厂/车间/生产线/设备/检测工位 */
    private String type;

    /** 排序号 */
    private Integer sort;

    /** 状态：1-启用 0-禁用 */
    private Integer status;

    /** 描述 */
    private String description;

    /** 子节点列表（用于树形结构，非数据库字段） */
    @TableField(exist = false)
    private java.util.List<Node> children;
}
