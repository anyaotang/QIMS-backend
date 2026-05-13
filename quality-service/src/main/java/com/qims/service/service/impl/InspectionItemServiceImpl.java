package com.qims.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qims.domain.entity.InspectionItem;
import com.qims.domain.entity.InspectionDefaultValue;
import com.qims.domain.mapper.InspectionItemMapper;
import com.qims.domain.mapper.InspectionDefaultValueMapper;
import com.qims.service.dto.InspectionItemDTO;
import com.qims.service.service.InspectionItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 检测项服务实现
 */
@Service
@RequiredArgsConstructor
public class InspectionItemServiceImpl extends ServiceImpl<InspectionItemMapper, InspectionItem> implements InspectionItemService {

    private final InspectionDefaultValueMapper defaultValueMapper;

    @Override
    public Page<InspectionItem> pageQuery(int page, int size, Long nodeId) {
        LambdaQueryWrapper<InspectionItem> wrapper = new LambdaQueryWrapper<>();
        if (nodeId != null) {
            wrapper.eq(InspectionItem::getNodeId, nodeId);
        }
        wrapper.orderByDesc(InspectionItem::getCreateTime);
        return baseMapper.selectPage(new Page<>(page, size), wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateItem(InspectionItemDTO dto) {
        InspectionItem item = new InspectionItem();
        BeanUtils.copyProperties(dto, item);

        if (item.getId() == null) {
            baseMapper.insert(item);
        } else {
            baseMapper.updateById(item);
        }

        // TODO: 若 isActive=true 且 dataSource 为 0 或 1，动态注册/更新 Quartz 任务
        // Quartz 管理在 quality-quartz 模块处理
    }

    @Override
    public Object getDefaultValueGroups(Long itemId) {
        List<InspectionDefaultValue> list = defaultValueMapper.selectList(
                new LambdaQueryWrapper<InspectionDefaultValue>().eq(InspectionDefaultValue::getItemId, itemId)
        );
        // 按 groupName 分组
        Map<String, List<InspectionDefaultValue>> groups = list.stream()
                .collect(Collectors.groupingBy(InspectionDefaultValue::getGroupName, LinkedHashMap::new, Collectors.toList()));
        return groups;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDefaultValueGroup(InspectionDefaultValue data) {
        defaultValueMapper.insert(data);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDefaultValueGroup(InspectionDefaultValue data) {
        defaultValueMapper.updateById(data);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDefaultValueGroup(String groupName) {
        defaultValueMapper.delete(
                new LambdaQueryWrapper<InspectionDefaultValue>().eq(InspectionDefaultValue::getGroupName, groupName)
        );
    }
}
