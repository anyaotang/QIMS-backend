package com.qims.web.controller;

import com.qims.common.result.R;
import com.qims.domain.entity.Department;
import com.qims.service.service.DepartmentService;
import lombok.RequiredArgsConstructor;
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
    public R<List<Department>> tree() {
        return R.ok(departmentService.getTree());
    }

    /**
     * 获取部门列表
     */
    @GetMapping("/list")
    public R<List<Department>> list() {
        return R.ok(departmentService.list());
    }

    /**
     * 创建部门
     */
    @PostMapping
    public R<Void> create(@RequestBody Department department) {
        departmentService.save(department);
        return R.ok();
    }

    /**
     * 更新部门
     */
    @PutMapping
    public R<Void> update(@RequestBody Department department) {
        departmentService.updateById(department);
        return R.ok();
    }

    /**
     * 删除部门
     */
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        departmentService.removeById(id);
        return R.ok();
    }

    /**
     * 设置部门继承角色
     */
    @PutMapping("/{id}/inherit-role")
    public R<Void> setInheritRole(@PathVariable Long id, @RequestBody List<Long> roleIds) {
        departmentService.setInheritRole(id, roleIds);
        return R.ok();
    }
}
