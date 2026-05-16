package com.qims.web.controller;

import com.qims.common.result.R;
import com.qims.service.dto.LoginRequest;
import com.qims.service.dto.LoginResponse;
import com.qims.service.dto.MenuDTO;
import com.qims.service.dto.RegisterRequest;
import com.qims.service.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 认证控制器
 */
@Tag(name = "01 认证管理", description = "用户登录、登出、注册、权限菜单获取")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "用户名密码登录", description = "通过用户名和密码进行认证，返回 JWT Token")
    @PostMapping("/login")
    public R<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return R.ok(response);
    }

    @Operation(summary = "登出", description = "JWT 无状态，前端删除 token 即可")
    @PostMapping("/logout")
    public R<Void> logout() {
        return R.ok();
    }

    @Operation(summary = "URL 参数登录", description = "支持 ?token=xxx 方式登录，token 已经由 JwtAuthenticationFilter 验证")
    @GetMapping("/url-login")
    public R<LoginResponse> urlLogin(@Parameter(description = "JWT Token") @RequestParam String token) {
        return R.ok();
    }

    @Operation(summary = "用户注册", description = "新用户注册")
    @PostMapping("/register")
    public R<Void> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return R.ok();
    }

    @Operation(summary = "获取当前用户权限列表", description = "返回当前登录用户拥有的所有权限编码")
    @GetMapping("/permissions")
    public R<List<String>> getPermissions(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return R.ok(authService.getCurrentUserPermissions(userId));
    }

    @Operation(summary = "获取当前用户菜单树", description = "返回当前登录用户的菜单树结构")
    @GetMapping("/menus")
    public R<List<MenuDTO>> getMenus(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return R.ok(authService.getUserMenus(userId));
    }

    @Operation(summary = "检查权限", description = "检查当前用户是否拥有指定权限")
    @GetMapping("/has-permission")
    public R<Boolean> hasPermission(HttpServletRequest request,
                                    @Parameter(description = "权限编码", example = "department:view") @RequestParam String code) {
        Long userId = (Long) request.getAttribute("userId");
        return R.ok(authService.hasPermission(userId, code));
    }
}
