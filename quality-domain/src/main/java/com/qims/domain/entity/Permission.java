package com.qims.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qims.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 权限实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_permission")
public class Permission extends BaseEntity {

    /** 权限名称 */
    private String name;

    /** 权限编码（如 node:edit） */
    private String code;

    /** 类型：1-菜单 2-按钮 3-接口 */
    private Integer type;

    /** 父权限ID */
    private Long parentId;

    /** 前端路由路径（菜单类型时填写） */
    private String path;

    /** 菜单图标名称（Element Plus 图标名） */
    private String icon;
}
