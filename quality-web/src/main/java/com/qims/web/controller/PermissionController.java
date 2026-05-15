package com.qims.web.controller;

import com.qims.common.result.R;
import com.qims.domain.entity.Permission;
import com.qims.service.dto.MenuDTO;
import com.qims.service.service.PermissionService;
import org.springframework.security.access.prepost.PreAuthorize;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限管理控制器
 */
@RestController
@RequestMapping("/api/permission")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    /**
     * 获取全部权限树（管理端用）
     */
    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('permission:view')")
    public R<List<MenuDTO>> tree() {
        return R.ok(permissionService.getAllPermissionTree());
    }

    /**
     * 获取当前用户菜单树
     */
    @GetMapping("/menus")
    public R<List<MenuDTO>> menus(jakarta.servlet.http.HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return R.ok(permissionService.getUserMenuTree(userId));
    }

    /**
     * 获取角色已分配的权限ID列表
     */
    @GetMapping("/role/{roleId}")
    @PreAuthorize("hasAuthority('role:assignPerm')")
    public R<List<Long>> rolePermissions(@PathVariable Long roleId) {
        return R.ok(permissionService.getRolePermissionIds(roleId));
    }

    /**
     * 为角色分配权限
     */
    @PostMapping("/role/{roleId}")
    @PreAuthorize("hasAuthority('role:assignPerm')")
    public R<Void> assignRolePermissions(@PathVariable Long roleId,
                                        @RequestBody List<Long> permissionIds) {
        permissionService.assignRolePermissions(roleId, permissionIds);
        return R.ok();
    }

    /**
     * 新增权限
     */
    @PostMapping
    @PreAuthorize("hasAuthority('permission:add')")
    public R<Void> add(@RequestBody Permission permission) {
        permissionService.save(permission);
        return R.ok();
    }

    /**
     * 编辑权限
     */
    @PutMapping
    @PreAuthorize("hasAuthority('permission:edit')")
    public R<Void> update(@RequestBody Permission permission) {
        permissionService.updateById(permission);
        return R.ok();
    }

    /**
     * 删除权限
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('permission:delete')")
    public R<Void> delete(@PathVariable Long id) {
        permissionService.removeById(id);
        return R.ok();
    }
}
