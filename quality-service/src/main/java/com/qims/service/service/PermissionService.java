package com.qims.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qims.domain.entity.Permission;

import java.util.List;

/**
 * 权限服务接口
 */
public interface PermissionService extends IService<Permission> {

    /**
     * 根据角色ID获取权限列表
     */
    List<Permission> listByRoleId(Long roleId);

    /**
     * 根据用户ID获取所有权限编码
     */
    List<String> listPermissionCodesByUserId(Long userId);
}
