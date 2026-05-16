package com.qims.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qims.common.result.R;
import com.qims.domain.entity.Role;
import com.qims.service.service.PermissionService;
import com.qims.service.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理控制器
 */
@Tag(name = "03 角色管理", description = "角色 CRUD、角色权限分配")
@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;
    private final PermissionService permissionService;

    @Operation(summary = "分页查询角色列表", description = "支持按名称或编码关键字搜索")
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('role:view')")
    public R<Page<Role>> page(@RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(required = false) String keyword) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<Role>()
                .like(keyword != null && !keyword.isEmpty(), Role::getName, keyword)
                .or(keyword != null && !keyword.isEmpty(), w -> w.like(Role::getCode, keyword))
                .orderByAsc(Role::getId);
        Page<Role> result = roleService.page(new Page<>(page, size), wrapper);
        return R.ok(result);
    }

    @Operation(summary = "获取所有角色列表", description = "不分页，用于下拉选择")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('role:view')")
    public R<List<Role>> list() {
        List<Role> roles = roleService.list(new LambdaQueryWrapper<Role>().orderByAsc(Role::getId));
        return R.ok(roles);
    }

    @Operation(summary = "获取角色详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('role:view')")
    public R<Role> getById(@PathVariable Long id) {
        return R.ok(roleService.getById(id));
    }

    @Operation(summary = "新增角色")
    @PostMapping
    @PreAuthorize("hasAuthority('role:add')")
    public R<Void> save(@RequestBody Role role) {
        roleService.save(role);
        return R.ok();
    }

    @Operation(summary = "更新角色")
    @PutMapping
    @PreAuthorize("hasAuthority('role:edit')")
    public R<Void> update(@RequestBody Role role) {
        roleService.updateById(role);
        return R.ok();
    }

    @Operation(summary = "删除角色")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('role:delete')")
    public R<Void> delete(@PathVariable Long id) {
        // TODO: 检查是否有关联用户，有则提示无法删除
        roleService.removeById(id);
        return R.ok();
    }

    @Operation(summary = "获取角色的权限ID列表", description = "用于权限配置弹窗回显")
    @GetMapping("/{roleId}/permissions")
    @PreAuthorize("hasAuthority('role:assignPerm')")
    public R<List<Long>> getRolePermissions(@PathVariable Long roleId) {
        return R.ok(permissionService.getRolePermissionIds(roleId));
    }

    @Operation(summary = "为角色分配权限", description = "替换指定角色的权限ID列表")
    @PutMapping("/{roleId}/permissions")
    @PreAuthorize("hasAuthority('role:assignPerm')")
    public R<Void> assignPermissions(@PathVariable Long roleId,
                                     @RequestBody List<Long> permissionIds) {
        permissionService.assignRolePermissions(roleId, permissionIds);
        return R.ok();
    }
}
