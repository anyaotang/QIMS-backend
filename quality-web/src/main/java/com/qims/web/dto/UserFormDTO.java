package com.qims.web.dto;

import com.qims.domain.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户表单 DTO（接收前端 userId 字段）
 */
@Data
@Schema(description = "用户表单请求")
public class UserFormDTO {

    @Schema(description = "用户ID（编辑时传入，新建时为空）")
    private Long userId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码（新建时必填，编辑时可选）")
    private String password;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "部门ID")
    private Long departmentId;

    @Schema(description = "角色ID列表")
    private Long[] roleIds;

    @Schema(description = "状态：1-启用 0-禁用")
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
