package com.qims.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qims.domain.entity.Permission;
import com.qims.domain.entity.RolePermission;
import com.qims.domain.entity.UserRole;
import com.qims.domain.mapper.PermissionMapper;
import com.qims.domain.mapper.RolePermissionMapper;
import com.qims.domain.mapper.UserRoleMapper;
import com.qims.service.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限服务实现
 */
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    private final RolePermissionMapper rolePermissionMapper;
    private final UserRoleMapper userRoleMapper;

    @Override
    public List<Permission> listByRoleId(Long roleId) {
        List<RolePermission> rpList = rolePermissionMapper.selectList(
                new LambdaQueryWrapper<RolePermission>().eq(RolePermission::getRoleId, roleId)
        );
        if (rpList.isEmpty()) {
            return List.of();
        }
        List<Long> permIds = rpList.stream().map(RolePermission::getPermissionId).collect(Collectors.toList());
        return baseMapper.selectBatchIds(permIds);
    }

    @Override
    public List<String> listPermissionCodesByUserId(Long userId) {
        List<UserRole> userRoles = userRoleMapper.selectList(
                new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId)
        );
        if (userRoles.isEmpty()) {
            return List.of();
        }
        List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<RolePermission> rps = rolePermissionMapper.selectList(
                new LambdaQueryWrapper<RolePermission>().in(RolePermission::getRoleId, roleIds)
        );
        if (rps.isEmpty()) {
            return List.of();
        }
        List<Long> permIds = rps.stream().map(RolePermission::getPermissionId).distinct().collect(Collectors.toList());
        List<Permission> perms = baseMapper.selectBatchIds(permIds);
        return perms.stream().map(Permission::getCode).collect(Collectors.toList());
    }
}
