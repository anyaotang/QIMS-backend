package com.qims.web.dto;

import com.qims.domain.entity.User;
import lombok.Data;

/**
 * 用户表单 DTO（接收前端 userId 字段）
 */
@Data
public class UserFormDTO {

    /** 用户ID（编辑时传入，新建时为空） */
    private Long userId;

    /** 用户名 */
    private String username;

    /** 密码（新建时必填，编辑时可选） */
    private String password;

    /** 昵称（对应实体的 realName） */
    private String nickname;

    /** 邮箱 */
    private String email;

    /** 手机号 */
    private String phone;

    /** 部门ID */
    private Long departmentId;

    /** 角色ID列表（暂未使用，角色关联通过角色管理模块处理） */
    private Long[] roleIds;

    /** 状态：1启用 0禁用 */
    private Integer status;

    /**
     * 转换为 User 实体
     */
    public User toEntity() {
        User user = new User();
        user.setId(userId);  // 前端的 userId → 实体的 id
        user.setUsername(username);
        user.setPassword(password);
        user.setRealName(nickname);  // 前端 nickname → 后端 realName
        user.setEmail(email);
        user.setPhone(phone);
        user.setDepartmentId(departmentId);
        user.setStatus(status != null ? status : 1);
        return user;
    }
}
