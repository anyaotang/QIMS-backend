package com.qims.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 登录响应 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private Long userId;
    private String username;
    private String realName;
    private Long departmentId;
    private List<String> roles;
    private List<String> permissions;
    private List<MenuDTO> menus;
}
