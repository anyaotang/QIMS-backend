package com.qims.web.controller;

import com.qims.common.result.R;
import com.qims.domain.entity.Department;
import com.qims.service.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理控制器
 */
@RestController
@RequestMapping("/api/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    /**
     * 获取部门树
     */
    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('department:view')")
    public R<List<Department>> tree() {
        return R.ok(departmentService.getTree());
    }

    /**
     * 获取部门列表
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('department:view')")
    public R<List<Department>> list() {
        return R.ok(departmentService.list());
    }

    /**
     * 创建部门
     */
    @PostMapping
    @PreAuthorize("hasAuthority('department:add')")
    public R<Void> create(@RequestBody Department department) {
        departmentService.save(department);
        return R.ok();
    }

    /**
     * 更新部门
     */
    @PutMapping
    @PreAuthorize("hasAuthority('department:edit')")
    public R<Void> update(@RequestBody Department department) {
        departmentService.updateById(department);
        return R.ok();
    }

    /**
     * 删除部门
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('department:delete')")
    public R<Void> delete(@PathVariable Long id) {
        departmentService.removeById(id);
        return R.ok();
    }

    /**
     * 设置部门继承角色
     */
    @PutMapping("/{id}/inherit-role")
    @PreAuthorize("hasAuthority('department:edit')")
    public R<Void> setInheritRole(@PathVariable Long id, @RequestBody List<Long> roleIds) {
        departmentService.setInheritRole(id, roleIds);
        return R.ok();
    }
}
