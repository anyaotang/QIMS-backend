package com.qims.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qims.domain.entity.User;

import java.util.List;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 获取部门下的所有用户
     */
    List<User> listByDepartmentId(Long departmentId);

    /**
     * 重置用户密码
     */
    void resetPassword(Long userId);

    /**
     * 获取用户权限列表
     */
    List<String> getCurrentUserPermissions(Long userId);

    /**
     * 检查用户是否有指定权限
     */
    boolean hasPermission(Long userId, String code);
}
