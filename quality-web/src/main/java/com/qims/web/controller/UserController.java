package com.qims.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qims.common.result.R;
import com.qims.domain.entity.User;
import com.qims.service.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 分页查询用户
     */
    @GetMapping("/page")
    public R<Page<User>> page(@RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(required = false) Long departmentId) {
        Page<User> result;
        if (departmentId != null) {
            result = userService.page(new Page<>(page, size),
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                    .eq(User::getDepartmentId, departmentId)
                    .eq(User::getStatus, 1));
        } else {
            result = userService.page(new Page<>(page, size),
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                    .eq(User::getStatus, 1));
        }
        return R.ok(result);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public R<User> getCurrentUser(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return R.ok(userService.getById(userId));
    }

    /**
     * 获取用户详情
     */
    @GetMapping("/{id}")
    public R<User> getById(@PathVariable Long id) {
        return R.ok(userService.getById(id));
    }

    /**
     * 新增用户
     */
    @PostMapping
    public R<Void> save(@RequestBody com.qims.web.dto.UserFormDTO dto) {
        userService.save(dto.toEntity());
        return R.ok();
    }

    /**
     * 更新用户
     */
    @PutMapping
    public R<Void> update(@RequestBody com.qims.web.dto.UserFormDTO dto) {
        userService.save(dto.toEntity());
        return R.ok();
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        userService.removeById(id);
        return R.ok();
    }

    /**
     * 重置用户密码
     */
    @PostMapping("/{id}/reset-password")
    public R<Void> resetPassword(@PathVariable Long id) {
        userService.resetPassword(id);
        return R.ok();
    }

    /**
     * 获取当前用户权限列表
     */
    @GetMapping("/permissions")
    public R<java.util.List<String>> permissions(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return R.ok(userService.getCurrentUserPermissions(userId));
    }

    /**
     * 检查当前用户是否有指定权限
     */
    @GetMapping("/has-permission")
    public R<Boolean> hasPermission(@RequestParam String code, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return R.ok(userService.hasPermission(userId, code));
    }
}
