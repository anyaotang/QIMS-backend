package com.qims.web.controller;

import com.qims.common.result.R;
import com.qims.domain.entity.Node;
import com.qims.service.service.NodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 节点管理控制器
 */
@RestController
@RequestMapping("/api/node")
@RequiredArgsConstructor
public class NodeController {

    private final NodeService nodeService;

    /**
     * 获取节点树
     */
    @GetMapping("/tree")
    public R<List<Node>> tree(@RequestParam(required = false) String type) {
        return R.ok(nodeService.getTree(type));
    }

    /**
     * 获取节点列表（平铺）
     */
    @GetMapping("/list")
    public R<List<Node>> list(@RequestParam(required = false) Integer type,
                              @RequestParam(required = false) Long parentId) {
        return R.ok(nodeService.listNodes(type, parentId));
    }

    /**
     * 新增节点
     */
    @PostMapping
    public R<Void> save(@RequestBody Node node) {
        nodeService.save(node);
        return R.ok();
    }

    /**
     * 更新节点
     */
    @PutMapping
    public R<Void> update(@RequestBody Node node) {
        nodeService.updateById(node);
        return R.ok();
    }

    /**
     * 删除节点
     */
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        nodeService.removeById(id);
        return R.ok();
    }

    /**
     * 移动节点（调整父子关系和排序）
     */
    @PostMapping("/{id}/move")
    public R<Void> move(@PathVariable Long id, @RequestBody java.util.Map<String, Object> body) {
        Long parentId = body.get("parentId") != null ? Long.valueOf(body.get("parentId").toString()) : null;
        Integer orderNum = body.get("orderNum") != null ? Integer.valueOf(body.get("orderNum").toString()) : 0;
        nodeService.moveNode(id, parentId, orderNum);
        return R.ok();
    }
}
