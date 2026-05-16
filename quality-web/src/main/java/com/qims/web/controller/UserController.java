package com.qims.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qims.common.datascope.DataScopeService;
import com.qims.common.result.R;
import com.qims.domain.entity.User;
import com.qims.service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制器
 */
@Tag(name = "08 用户管理", description = "用户 CRUD、当前用户信息、密码重置、权限查询")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final DataScopeService dataScopeService;

    @Operation(summary = "分页查询用户", description = "支持按部门ID过滤，无部门ID时根据数据权限自动过滤")
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('user:view')")
    public R<Page<User>> page(@RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(required = false) Long departmentId) {
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User> wrapper =
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                .eq(User::getStatus, 1);
        if (departmentId != null) {
            wrapper.eq(User::getDepartmentId, departmentId);
        } else {
            // 没有指定部门ID时，根据数据权限自动过滤
            dataScopeService.applyDeptScope(wrapper, User::getDepartmentId);
        }
        Page<User> result = userService.page(new Page<>(page, size), wrapper);
        return R.ok(result);
    }

    @Operation(summary = "获取当前用户信息", description = "根据 JWT Token 返回当前登录用户的详细信息")
    @GetMapping("/info")
    public R<User> getCurrentUser(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return R.ok(userService.getById(userId));
    }

    @Operation(summary = "获取用户详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('user:view')")
    public R<User> getById(@PathVariable Long id) {
        return R.ok(userService.getById(id));
    }

    @Operation(summary = "新增用户")
    @PostMapping
    @PreAuthorize("hasAuthority('user:add')")
    public R<Void> save(@RequestBody com.qims.web.dto.UserFormDTO dto) {
        userService.save(dto.toEntity());
        return R.ok();
    }

    @Operation(summary = "更新用户")
    @PutMapping
    @PreAuthorize("hasAuthority('user:edit')")
    public R<Void> update(@RequestBody com.qims.web.dto.UserFormDTO dto) {
        userService.save(dto.toEntity());
        return R.ok();
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user:delete')")
    public R<Void> delete(@PathVariable Long id) {
        userService.removeById(id);
        return R.ok();
    }

    @Operation(summary = "重置用户密码")
    @PostMapping("/{id}/reset-password")
    @PreAuthorize("hasAuthority('user:resetPwd')")
    public R<Void> resetPassword(@PathVariable Long id) {
        userService.resetPassword(id);
        return R.ok();
    }

    @Operation(summary = "获取当前用户权限列表")
    @GetMapping("/permissions")
    public R<java.util.List<String>> permissions(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return R.ok(userService.getCurrentUserPermissions(userId));
    }

    @Operation(summary = "检查当前用户是否有指定权限")
    @GetMapping("/has-permission")
    public R<Boolean> hasPermission(@RequestParam String code, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return R.ok(userService.hasPermission(userId, code));
    }
}
