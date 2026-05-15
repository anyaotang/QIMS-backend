package com.qims.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qims.domain.entity.Node;
import com.qims.domain.mapper.NodeMapper;
import com.qims.service.service.NodeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 节点服务实现
 */
@Service
public class NodeServiceImpl extends ServiceImpl<NodeMapper, Node> implements NodeService {

    @Override
    public List<Node> getTree(String type) {
        LambdaQueryWrapper<Node> wrapper = new LambdaQueryWrapper<Node>()
                .eq(Node::getStatus, 1)
                .orderByAsc(Node::getSort);
        if (type != null && !type.isEmpty()) {
            wrapper.eq(Node::getType, type);
        }
        List<Node> all = baseMapper.selectList(wrapper);
        return buildTree(all, 0L);
    }

    private List<Node> buildTree(List<Node> all, Long parentId) {
        return all.stream()
                .filter(n -> parentId.equals(n.getParentId()))
                .peek(n -> n.setChildren(buildTree(all, n.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<Node> listNodes(String type, Long parentId) {
        LambdaQueryWrapper<Node> wrapper = new LambdaQueryWrapper<>();
        if (type != null) {
            wrapper.eq(Node::getType, type);
        }
        if (parentId != null) {
            wrapper.eq(Node::getParentId, parentId);
        }
        wrapper.eq(Node::getStatus, 1).orderByAsc(Node::getSort);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public void moveNode(Long id, Long parentId, Integer orderNum) {
        Node node = baseMapper.selectById(id);
        if (node == null) {
            throw new com.qims.common.exception.BizException(404, "节点不存在");
        }
        if (parentId != null) {
            node.setParentId(parentId);
        }
        if (orderNum != null) {
            node.setSort(orderNum);
        }
        baseMapper.updateById(node);
    }
}
