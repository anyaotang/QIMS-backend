package com.qims.web.controller;

import com.qims.common.result.R;
import com.qims.domain.entity.Node;
import com.qims.service.service.NodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 节点管理控制器
 */
@Tag(name = "05 节点管理", description = "节点（工厂/车间/生产线/设备/检测工位）CRUD、节点树、移动节点")
@RestController
@RequestMapping("/api/node")
@RequiredArgsConstructor
public class NodeController {

    private final NodeService nodeService;

    @Operation(summary = "获取节点树", description = "返回树形结构节点列表，支持按类型过滤")
    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('node:view')")
    public R<List<Node>> tree(@RequestParam(required = false) String type) {
        return R.ok(nodeService.getTree(type));
    }

    @Operation(summary = "获取节点列表（平铺）", description = "支持按类型和父节点ID过滤")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('node:view')")
    public R<List<Node>> list(@RequestParam(required = false) String type,
                              @RequestParam(required = false) Long parentId) {
        return R.ok(nodeService.listNodes(type, parentId));
    }

    @Operation(summary = "新增节点")
    @PostMapping
    @PreAuthorize("hasAuthority('node:add')")
    public R<Void> save(@RequestBody Node node) {
        nodeService.save(node);
        return R.ok();
    }

    @Operation(summary = "更新节点")
    @PutMapping
    @PreAuthorize("hasAuthority('node:edit')")
    public R<Void> update(@RequestBody Node node) {
        nodeService.updateById(node);
        return R.ok();
    }

    @Operation(summary = "删除节点")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('node:delete')")
    public R<Void> delete(@PathVariable Long id) {
        nodeService.removeById(id);
        return R.ok();
    }

    @Operation(summary = "移动节点", description = "调整节点的父子关系和排序")
    @PostMapping("/{id}/move")
    @PreAuthorize("hasAuthority('node:edit')")
    public R<Void> move(@PathVariable Long id, @RequestBody java.util.Map<String, Object> body) {
        Long parentId = body.get("parentId") != null ? Long.valueOf(body.get("parentId").toString()) : null;
        Integer orderNum = body.get("orderNum") != null ? Integer.valueOf(body.get("orderNum").toString()) : 0;
        nodeService.moveNode(id, parentId, orderNum);
        return R.ok();
    }
}
