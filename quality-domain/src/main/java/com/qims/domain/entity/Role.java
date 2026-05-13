package com.qims.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qims.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
public class Role extends BaseEntity {

    /** 角色名称 */
    private String name;

    /** 角色编码 */
    private String code;

    /** 状态：1启用 0禁用 */
    private Integer status;

    /** 备注 */
    private String remark;
}
