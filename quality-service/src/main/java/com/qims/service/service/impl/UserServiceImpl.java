package com.qims.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qims.domain.entity.User;
import com.qims.domain.mapper.UserMapper;
import com.qims.service.service.AuthService;
import com.qims.service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户服务实现
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    @Override
    public boolean save(User user) {
        // 检查用户名是否已存在
        Long existingId = baseMapper.selectIdByUsername(user.getUsername());
        if (existingId != null && !existingId.equals(user.getId())) {
            throw new com.qims.common.exception.BizException("用户名已存在");
        }
        // 密码加密
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        // 根据 id 是否为空决定更新还是插入
        if (user.getId() != null) {
            return baseMapper.updateById(user) > 0;
        } else {
            // 新建时默认启用
            if (user.getStatus() == null) {
                user.setStatus(1);
            }
            baseMapper.insert(user);
        }
        return true;
    }

    @Override
    public List<User> listByDepartmentId(Long departmentId) {
        return baseMapper.selectList(
                new LambdaQueryWrapper<User>()
                        .eq(User::getDepartmentId, departmentId)
                        .eq(User::getStatus, 1)
        );
    }

    @Override
    public void resetPassword(Long userId) {
        User user = baseMapper.selectById(userId);
        if (user == null) {
            throw new com.qims.common.exception.BizException("用户不存在");
        }
        user.setPassword(passwordEncoder.encode("123456"));
        baseMapper.updateById(user);
    }

    /**
     * 根据用户名查 ID（用于检查重复）
     */
    public Long selectIdByUsername(String username) {
        return baseMapper.selectIdByUsername(username);
    }

    @Override
    public List<String> getCurrentUserPermissions(Long userId) {
        return authService.getCurrentUserPermissions(userId);
    }

    @Override
    public boolean hasPermission(Long userId, String code) {
        return authService.hasPermission(userId, code);
    }
}
