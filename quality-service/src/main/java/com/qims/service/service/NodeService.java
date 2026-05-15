package com.qims.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qims.domain.entity.Node;

import java.util.List;

/**
 * 节点服务接口
 */
public interface NodeService extends IService<Node> {

    /**
     * 获取节点树
     * @param type 节点类型过滤（可选）
     */
    List<Node> getTree(String type);

    /**
     * 获取节点列表（平铺）
     */
    List<Node> listNodes(String type, Long parentId);

    /**
     * 移动节点
     */
    void moveNode(Long id, Long parentId, Integer orderNum);
}
