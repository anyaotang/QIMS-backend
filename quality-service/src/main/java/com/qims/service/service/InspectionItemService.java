package com.qims.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qims.domain.entity.InspectionItem;
import com.qims.service.dto.InspectionItemDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 检测项服务接口
 */
public interface InspectionItemService extends IService<InspectionItem> {

    /**
     * 分页查询检测项
     */
    Page<InspectionItem> pageQuery(int page, int size, Long nodeId);

    /**
     * 新增或更新检测项（同时管理 Quartz 任务）
     */
    void saveOrUpdateItem(InspectionItemDTO dto);

    /**
     * 默认值分组管理
     */
    Object getDefaultValueGroups(Long itemId);

    /**
     * 保存默认值分组
     */
    void saveDefaultValueGroup(com.qims.domain.entity.InspectionDefaultValue data);

    /**
     * 更新默认值分组
     */
    void updateDefaultValueGroup(com.qims.domain.entity.InspectionDefaultValue data);

    /**
     * 删除默认值分组
     */
    void deleteDefaultValueGroup(String groupName);
}
