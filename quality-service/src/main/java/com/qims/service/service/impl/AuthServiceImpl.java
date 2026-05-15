package com.qims.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qims.common.exception.BizException;
import com.qims.common.security.JwtTokenProvider;
import com.qims.domain.entity.*;
import com.qims.domain.mapper.*;
import com.qims.service.dto.*;
import com.qims.service.service.AuthService;
import com.qims.service.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 认证服务实现
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl extends ServiceImpl<UserMapper, User> implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRoleMapper userRoleMapper;
    private final RoleMapper roleMapper;
    private final RolePermissionMapper rolePermissionMapper;
    private final PermissionMapper permissionMapper;
    private final PermissionService permissionService;

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = getByUsername(request.getUsername());
        if (user == null) {
            throw new BizException(401, "用户名或密码错误");
        }
        if (user.getDeleted() == 1) {
            throw new BizException(403, "账号已被删除");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BizException(403, "账号已被禁用，请联系管理员");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BizException(401, "用户名或密码错误");
        }

        // 获取角色和权限
        List<String> roles = getUserRoles(user.getId());
        List<String> permissions = listPermissionCodesByUserId(user.getId());

        // 计算数据范围（取角色中最宽松的，即 data_scope 最小值）
        int dataScope = getUserDataScope(user.getId());

        // 生成 JWT
        String token = jwtTokenProvider.generateToken(
                user.getId(),
                user.getUsername(),
                user.getDepartmentId(),
                roles,
                permissions,
                dataScope
        );

        return LoginResponse.builder()
                .token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .realName(user.getRealName())
                .departmentId(user.getDepartmentId())
                .roles(roles)
                .permissions(permissions)
                .menus(permissionService.getUserMenuTree(user.getId()))
                .build();
    }

    @Override
    public void register(RegisterRequest request) {
        // 检查用户名是否已存在
        if (getByUsername(request.getUsername()) != null) {
            throw new BizException(400, "用户名已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRealName(request.getRealName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setDepartmentId(request.getDepartmentId());
        user.setStatus(1);
        baseMapper.insert(user);
    }

    @Override
    public List<String> getCurrentUserPermissions(Long userId) {
        return listPermissionCodesByUserId(userId);
    }

    @Override
    public boolean hasPermission(Long userId, String code) {
        List<String> permissions = getCurrentUserPermissions(userId);
        return permissions.contains(code);
    }

    @Override
    public User getByUsername(String username) {
        return baseMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, username)
        );
    }

    /**
     * 获取用户数据范围（取角色中最宽松的，data_scope 最小值）
     * 1=全部 2=本部门 3=本部门及子部门 4=仅本人
     */
    private int getUserDataScope(Long userId) {
        List<UserRole> userRoles = userRoleMapper.selectList(
                new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId)
        );
        if (userRoles.isEmpty()) {
            return 4; // 无角色默认仅本人
        }
        List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<Role> roles = roleMapper.selectBatchIds(roleIds);
        // 取 data_scope 最小值（最宽松）
        return roles.stream()
                .mapToInt(r -> r.getDataScope() != null ? r.getDataScope() : 4)
                .min()
                .orElse(4);
    }

    /**
     * 获取用户角色列表
     */
    private List<String> getUserRoles(Long userId) {
        List<UserRole> userRoles = userRoleMapper.selectList(
                new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId)
        );
        if (userRoles.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<Role> roles = roleMapper.selectBatchIds(roleIds);
        return roles.stream().map(Role::getCode).collect(Collectors.toList());
    }

    /**
     * 获取用户所有权限编码（含角色权限）
     */
    private List<String> listPermissionCodesByUserId(Long userId) {
        // 1. 获取用户角色
        List<UserRole> userRoles = userRoleMapper.selectList(
                new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId)
        );
        if (userRoles.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());

        // 2. 获取角色关联的权限ID
        List<RolePermission> rolePermissions = rolePermissionMapper.selectList(
                new LambdaQueryWrapper<RolePermission>().in(RolePermission::getRoleId, roleIds)
        );
        if (rolePermissions.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> permissionIds = rolePermissions.stream()
                .map(RolePermission::getPermissionId).distinct().collect(Collectors.toList());

        // 3. 获取权限编码
        List<Permission> permissions = permissionMapper.selectBatchIds(permissionIds);
        return permissions.stream().map(Permission::getCode).collect(Collectors.toList());
    }

    
    @Override
    public List<MenuDTO> getUserMenus(Long userId) {
        return permissionService.getUserMenuTree(userId);
    }
}
