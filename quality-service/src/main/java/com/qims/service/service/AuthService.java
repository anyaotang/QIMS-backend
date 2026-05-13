package com.qims.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qims.domain.entity.User;
import com.qims.service.dto.LoginRequest;
import com.qims.service.dto.LoginResponse;
import com.qims.service.dto.MenuDTO;
import com.qims.service.dto.RegisterRequest;

import java.util.List;

/**
 * 认证服务接口
 */
public interface AuthService extends IService<User> {

    /**
     * 用户名密码登录
     */
    LoginResponse login(LoginRequest request);

    /**
     * 注册
     */
    void register(RegisterRequest request);

    /**
     * 获取当前用户所有权限（含继承）
     */
    List<String> getCurrentUserPermissions(Long userId);

    /**
     * 检查是否有某权限
     */
    boolean hasPermission(Long userId, String code);

    /**
     * 根据用户名获取用户
     */
    User getByUsername(String username);

    /**
     * 获取当前用户的菜单树
     */
    List<MenuDTO> getUserMenus(Long userId);
}
