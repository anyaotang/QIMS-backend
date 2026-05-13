package com.qims.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qims.common.exception.BizException;
import com.qims.common.security.JwtTokenProvider;
import com.qims.domain.entity.*;
import com.qims.domain.mapper.*;
import com.qims.service.dto.*;
import com.qims.service.service.AuthService;
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

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = getByUsername(request.getUsername());
        if (user == null) {
            throw new BizException("用户名或密码错误");
        }
        if (user.getDeleted() == 1) {
            throw new BizException("账号已被禁用");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BizException("用户名或密码错误");
        }

        // 获取角色和权限
        List<String> roles = getUserRoles(user.getId());
        List<String> permissions = listPermissionCodesByUserId(user.getId());

        // 生成 JWT
        String token = jwtTokenProvider.generateToken(
                user.getId(),
                user.getUsername(),
                user.getDepartmentId(),
                roles,
                permissions
        );

        return LoginResponse.builder()
                .token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .realName(user.getRealName())
                .departmentId(user.getDepartmentId())
                .roles(roles)
                .permissions(permissions)
                .build();
    }

    @Override
    public void register(RegisterRequest request) {
        // 检查用户名是否已存在
        if (getByUsername(request.getUsername()) != null) {
            throw new BizException("用户名已存在");
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
        // 1. 查询用户所有权限（含父级节点）
        List<Permission> allPermissions = listPermissionsByUserId(userId);
        if (allPermissions.isEmpty()) {
            return new ArrayList<>();
        }

        // 2. 转换为 MenuDTO 列表（填充 path 和 icon）
        List<MenuDTO> menuList = allPermissions.stream().map(p -> {
            String code = p.getCode();
            Integer typeVal = p.getType() != null ? p.getType() : 1;
            Long parentIdVal = p.getParentId();
            return MenuDTO.builder()
                    .id(p.getId())
                    .name(p.getName())
                    .code(code)
                    .type(typeVal)
                    .parentId(parentIdVal)
                    .path(p.getPath())
                    .icon(p.getIcon())
                    .children(new ArrayList<>())
                    .build();
        }).collect(Collectors.toList());

        // 3. 构建树形结构（parent_id=null 的为根节点）
        return buildMenuTree(menuList);
    }

    /**
     * 查询用户关联的所有权限实体（包含父级菜单节点）
     */
    private List<Permission> listPermissionsByUserId(Long userId) {
        // 获取用户角色
        List<UserRole> userRoles = userRoleMapper.selectList(
                new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId)
        );
        if (userRoles.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());

        // 获取角色关联的权限ID
        List<RolePermission> rolePermissions = rolePermissionMapper.selectList(
                new LambdaQueryWrapper<RolePermission>().in(RolePermission::getRoleId, roleIds)
        );
        if (rolePermissions.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> permissionIds = rolePermissions.stream()
                .map(RolePermission::getPermissionId).distinct().collect(Collectors.toList());

        // 查询所有权限实体
        return permissionMapper.selectBatchIds(permissionIds);
    }

    /**
     * 构建菜单树：将平铺列表转为层级结构
     */
    private List<MenuDTO> buildMenuTree(List<MenuDTO> menuList) {
        List<MenuDTO> roots = new ArrayList<>();
        java.util.Map<Long, MenuDTO> menuMap = new java.util.HashMap<>();

        // 建立映射
        for (MenuDTO menu : menuList) {
            menuMap.put(menu.getId(), menu);
        }

        // 组装树
        for (MenuDTO menu : menuList) {
            if (menu.getParentId() == null || menu.getParentId() == 0L) {
                roots.add(menu);
            } else {
                MenuDTO parent = menuMap.get(menu.getParentId());
                if (parent != null) {
                    parent.getChildren().add(menu);
                } else {
                    // 父级不在列表中，作为根节点
                    roots.add(menu);
                }
            }
        }

        // 排序：按 id 升序
        roots.sort((a, b) -> a.getId().compareTo(b.getId()));
        for (MenuDTO root : roots) {
            sortChildren(root);
        }

        return roots;
    }

    /**
     * 递归排序子菜单
     */
    private void sortChildren(MenuDTO menu) {
        if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
            menu.getChildren().sort((a, b) -> a.getId().compareTo(b.getId()));
            for (MenuDTO child : menu.getChildren()) {
                sortChildren(child);
            }
        }
    }
}
