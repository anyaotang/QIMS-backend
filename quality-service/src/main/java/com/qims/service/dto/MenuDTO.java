package com.qims.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "菜单树节点")
public class MenuDTO {

    @Schema(description = "菜单ID")
    private Long id;

    @Schema(description = "菜单名称")
    private String name;

    @Schema(description = "权限编码")
    private String code;

    @Schema(description = "类型：1-目录 2-菜单 3-按钮")
    private Integer type;

    @Schema(description = "父级ID")
    private Long parentId;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "路由路径")
    private String path;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "前端组件路径")
    private String component;

    @Schema(description = "子菜单列表")
    private List<MenuDTO> children;
}
