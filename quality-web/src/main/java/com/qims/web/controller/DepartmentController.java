package com.qims.web.controller;

import com.qims.common.result.R;
import com.qims.domain.entity.Department;
import com.qims.service.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理控制器
 */
@Tag(name = "02 部门管理", description = "部门 CRUD、部门树、继承角色设置")
@RestController
@RequestMapping("/api/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @Operation(summary = "获取部门树", description = "返回树形结构的部门列表")
    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('department:view')")
    public R<List<Department>> tree() {
        return R.ok(departmentService.getTree());
    }

    @Operation(summary = "获取部门列表", description = "返回平铺的部门列表")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('department:view')")
    public R<List<Department>> list() {
        return R.ok(departmentService.list());
    }

    @Operation(summary = "创建部门")
    @PostMapping
    @PreAuthorize("hasAuthority('department:add')")
    public R<Void> create(@RequestBody Department department) {
        departmentService.save(department);
        return R.ok();
    }

    @Operation(summary = "更新部门")
    @PutMapping
    @PreAuthorize("hasAuthority('department:edit')")
    public R<Void> update(@RequestBody Department department) {
        departmentService.updateById(department);
        return R.ok();
    }

    @Operation(summary = "删除部门")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('department:delete')")
    public R<Void> delete(@Parameter(description = "部门ID") @PathVariable Long id) {
        departmentService.removeById(id);
        return R.ok();
    }

    @Operation(summary = "设置部门继承角色", description = "为部门设置继承的角色ID列表")
    @PutMapping("/{id}/inherit-role")
    @PreAuthorize("hasAuthority('department:edit')")
    public R<Void> setInheritRole(@Parameter(description = "部门ID") @PathVariable Long id,
                                  @RequestBody List<Long> roleIds) {
        departmentService.setInheritRole(id, roleIds);
        return R.ok();
    }
}
