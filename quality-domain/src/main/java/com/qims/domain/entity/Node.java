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

    /** 节点类型：0-部门 1-产品 2-工序 3-检测点 */
    private Integer type;

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
