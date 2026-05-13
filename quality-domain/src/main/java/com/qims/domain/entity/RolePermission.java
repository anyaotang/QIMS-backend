package com.qims.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qims.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色权限关联实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role_permission")
public class RolePermission extends BaseEntity {

    private Long roleId;
    private Long permissionId;
}
