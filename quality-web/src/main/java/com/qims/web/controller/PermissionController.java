package com.qims.web.controller;

import com.qims.common.result.R;
import com.qims.domain.entity.Permission;
import com.qims.service.dto.MenuDTO;
import com.qims.service.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限管理控制器
 */
@Tag(name = "04 权限管理", description = "权限 CRUD、权限树、菜单树、角色权限分配")
@RestController
@RequestMapping("/api/permission")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @Operation(summary = "获取全部权限树", description = "返回管理端使用的完整权限树")
    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('permission:view')")
    public R<List<MenuDTO>> tree() {
        return R.ok(permissionService.getAllPermissionTree());
    }

    @Operation(summary = "获取当前用户菜单树", description = "根据登录用户ID返回其有权限的菜单树")
    @GetMapping("/menus")
    public R<List<MenuDTO>> menus(jakarta.servlet.http.HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return R.ok(permissionService.getUserMenuTree(userId));
    }

    @Operation(summary = "获取角色已分配的权限ID列表")
    @GetMapping("/role/{roleId}")
    @PreAuthorize("hasAuthority('role:assignPerm')")
    public R<List<Long>> rolePermissions(@PathVariable Long roleId) {
        return R.ok(permissionService.getRolePermissionIds(roleId));
    }

    @Operation(summary = "为角色分配权限", description = "替换指定角色的权限ID列表")
    @PostMapping("/role/{roleId}")
    @PreAuthorize("hasAuthority('role:assignPerm')")
    public R<Void> assignRolePermissions(@PathVariable Long roleId,
                                        @RequestBody List<Long> permissionIds) {
        permissionService.assignRolePermissions(roleId, permissionIds);
        return R.ok();
    }

    @Operation(summary = "新增权限")
    @PostMapping
    @PreAuthorize("hasAuthority('permission:add')")
    public R<Void> add(@RequestBody Permission permission) {
        permissionService.save(permission);
        return R.ok();
    }

    @Operation(summary = "编辑权限")
    @PutMapping
    @PreAuthorize("hasAuthority('permission:edit')")
    public R<Void> update(@RequestBody Permission permission) {
        permissionService.updateById(permission);
        return R.ok();
    }

    @Operation(summary = "删除权限")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('permission:delete')")
    public R<Void> delete(@PathVariable Long id) {
        permissionService.removeById(id);
        return R.ok();
    }
}
