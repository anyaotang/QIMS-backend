package com.qims.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qims.domain.entity.Permission;
import com.qims.service.dto.MenuDTO;

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

    /**
     * 获取用户菜单树（只含 type=1 目录 + type=2 菜单，不含按钮）
     */
    List<MenuDTO> getUserMenuTree(Long userId);

    /**
     * 获取所有权限树（管理端用）
     */
    List<MenuDTO> getAllPermissionTree();

    /**
     * 为角色分配权限
     */
    void assignRolePermissions(Long roleId, List<Long> permissionIds);

    /**
     * 获取角色的权限ID列表
     */
    List<Long> getRolePermissionIds(Long roleId);
}
