package com.qims.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 菜单树 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuDTO {

    /** 菜单ID */
    private Long id;

    /** 菜单名称 */
    private String name;

    /** 权限编码 */
    private String code;

    /** 类型：1-菜单 2-按钮 */
    private Integer type;

    /** 父级ID */
    private Long parentId;

    /** 排序 */
    private Integer sort;

    /** 路由路径（前端路由） */
    private String path;

    /** 图标 */
    private String icon;

    /** 子菜单列表 */
    private List<MenuDTO> children;
}
