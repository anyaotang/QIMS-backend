package com.qims.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qims.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 部门实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_department")
public class Department extends BaseEntity {

    /** 部门名称 */
    private String name;

    /** 父部门ID，0 为根节点 */
    private Long parentId;

    /** 排序号 */
    private Integer sort;

    /** 状态：1-启用 0-禁用 */
    private Integer status;

    /** 继承角色ID（逗号分隔） */
    private String inheritRoleIds;

    /** 子部门列表（用于树形结构，非数据库字段） */
    @TableField(exist = false)
    private java.util.List<Department> children;
}
