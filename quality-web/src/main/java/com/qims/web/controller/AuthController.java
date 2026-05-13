package com.qims.web.controller;

import com.qims.common.result.R;
import com.qims.service.dto.LoginRequest;
import com.qims.service.dto.LoginResponse;
import com.qims.service.dto.MenuDTO;
import com.qims.service.dto.RegisterRequest;
import com.qims.service.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 用户名密码登录
     */
    @PostMapping("/login")
    public R<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return R.ok(response);
    }

    /**
     * 登出
     */
    @PostMapping("/logout")
    public R<Void> logout() {
        // JWT 无状态，前端删除 token 即可
        return R.ok();
    }

    /**
     * URL 参数登录（支持 ?token=xxx）
     */
    @GetMapping("/url-login")
    public R<LoginResponse> urlLogin(@RequestParam String token) {
        // token 已经由 JwtAuthenticationFilter 验证
        // 此处返回前端首页所需信息
        return R.ok();
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public R<Void> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return R.ok();
    }

    /**
     * 获取当前用户权限
     */
    @GetMapping("/permissions")
    public R<List<String>> getPermissions(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return R.ok(authService.getCurrentUserPermissions(userId));
    }

    /**
     * 获取当前用户菜单树
     */
    @GetMapping("/menus")
    public R<List<MenuDTO>> getMenus(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return R.ok(authService.getUserMenus(userId));
    }

    /**
     * 检查是否有某权限
     */
    @GetMapping("/has-permission")
    public R<Boolean> hasPermission(HttpServletRequest request, @RequestParam String code) {
        Long userId = (Long) request.getAttribute("userId");
        return R.ok(authService.hasPermission(userId, code));
    }
}
