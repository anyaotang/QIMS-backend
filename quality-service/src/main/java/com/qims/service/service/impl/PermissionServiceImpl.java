package com.qims.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qims.common.exception.BizException;
import com.qims.domain.entity.Permission;
import com.qims.domain.entity.RolePermission;
import com.qims.domain.entity.UserRole;
import com.qims.domain.mapper.PermissionMapper;
import com.qims.domain.mapper.RolePermissionMapper;
import com.qims.domain.mapper.UserRoleMapper;
import com.qims.service.dto.MenuDTO;
import com.qims.service.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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

    @Override
    public List<MenuDTO> getUserMenuTree(Long userId) {
        // 1. 查询用户关联的所有权限ID
        List<Long> permIds = getPermissionIdsByUserId(userId);
        if (permIds.isEmpty()) {
            return new ArrayList<>();
        }

        // 2. 查询所有权限实体
        List<Permission> userPerms = baseMapper.selectBatchIds(permIds);
        Set<Long> userPermIdSet = userPerms.stream().map(Permission::getId).collect(Collectors.toSet());

        // 3. 补充父级节点（确保树形结构完整）
        Set<Long> allNeededIds = new HashSet<>(userPermIdSet);
        for (Permission p : userPerms) {
            if (p.getParentId() != null && p.getParentId() != 0L) {
                allNeededIds.add(p.getParentId());
            }
        }

        // 4. 查询完整数据（仅目录和菜单，不含按钮）
        List<Permission> allPerms = baseMapper.selectList(
                new LambdaQueryWrapper<Permission>()
                        .in(Permission::getId, allNeededIds)
                        .in(Permission::getType, 1, 2)
                        .eq(Permission::getStatus, 1)
                        .orderByAsc(Permission::getSort)
        );

        // 5. 转换并构建树
        List<MenuDTO> menuList = allPerms.stream().map(this::toMenuDTO).collect(Collectors.toList());
        return buildTree(menuList);
    }

    @Override
    public List<MenuDTO> getAllPermissionTree() {
        List<Permission> allPerms = baseMapper.selectList(
                new LambdaQueryWrapper<Permission>()
                        .eq(Permission::getStatus, 1)
                        .orderByAsc(Permission::getType)
                        .orderByAsc(Permission::getSort)
        );
        List<MenuDTO> menuList = allPerms.stream().map(this::toMenuDTO).collect(Collectors.toList());
        return buildTree(menuList);
    }

    @Override
    @Transactional
    public void assignRolePermissions(Long roleId, List<Long> permissionIds) {
        // 删除旧关联
        rolePermissionMapper.delete(
                new LambdaQueryWrapper<RolePermission>().eq(RolePermission::getRoleId, roleId)
        );
        // 插入新关联
        if (permissionIds != null && !permissionIds.isEmpty()) {
            for (Long permId : permissionIds) {
                RolePermission rp = new RolePermission();
                rp.setRoleId(roleId);
                rp.setPermissionId(permId);
                rolePermissionMapper.insert(rp);
            }
        }
    }

    @Override
    public List<Long> getRolePermissionIds(Long roleId) {
        List<RolePermission> rps = rolePermissionMapper.selectList(
                new LambdaQueryWrapper<RolePermission>().eq(RolePermission::getRoleId, roleId)
        );
        return rps.stream().map(RolePermission::getPermissionId).collect(Collectors.toList());
    }

    // ========== 私有方法 ==========

    private List<Long> getPermissionIdsByUserId(Long userId) {
        List<UserRole> userRoles = userRoleMapper.selectList(
                new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId)
        );
        if (userRoles.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<RolePermission> rps = rolePermissionMapper.selectList(
                new LambdaQueryWrapper<RolePermission>().in(RolePermission::getRoleId, roleIds)
        );
        return rps.stream().map(RolePermission::getPermissionId).distinct().collect(Collectors.toList());
    }

    private MenuDTO toMenuDTO(Permission p) {
        return MenuDTO.builder()
                .id(p.getId())
                .name(p.getName())
                .code(p.getCode())
                .type(p.getType())
                .parentId(p.getParentId())
                .sort(p.getSort())
                .path(p.getPath())
                .icon(p.getIcon())
                .component(p.getComponent())
                .children(new ArrayList<>())
                .build();
    }

    private List<MenuDTO> buildTree(List<MenuDTO> menuList) {
        Map<Long, MenuDTO> menuMap = new LinkedHashMap<>();
        for (MenuDTO menu : menuList) {
            menuMap.put(menu.getId(), menu);
        }

        List<MenuDTO> roots = new ArrayList<>();
        for (MenuDTO menu : menuList) {
            if (menu.getParentId() == null || menu.getParentId() == 0L) {
                roots.add(menu);
            } else {
                MenuDTO parent = menuMap.get(menu.getParentId());
                if (parent != null) {
                    parent.getChildren().add(menu);
                } else {
                    roots.add(menu);
                }
            }
        }
        // 根节点按 sort 排序
        roots.sort(Comparator.comparingInt(m -> m.getSort() != null ? m.getSort() : 0));
        // 递归排序子节点
        for (MenuDTO root : roots) {
            sortChildren(root);
        }
        return roots;
    }

    private void sortChildren(MenuDTO menu) {
        if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
            menu.getChildren().sort(Comparator.comparingInt(m -> m.getSort() != null ? m.getSort() : 0));
            for (MenuDTO child : menu.getChildren()) {
                sortChildren(child);
            }
        }
    }
}
