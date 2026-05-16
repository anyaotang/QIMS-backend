package com.qims.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qims.common.result.R;
import com.qims.domain.entity.InspectionItem;
import com.qims.quartz.service.QuartzScheduleService;
import com.qims.service.dto.InspectionItemDTO;
import com.qims.service.service.InspectionItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 检测项管理控制器
 */
@Tag(name = "06 检测项管理", description = "检测项 CRUD、启用/禁用、默认值分组管理")
@RestController
@RequestMapping("/api/inspectionitem")
@RequiredArgsConstructor
public class InspectionItemController {

    private final InspectionItemService inspectionItemService;
    private final QuartzScheduleService quartzScheduleService;

    @Operation(summary = "分页查询检测项", description = "按检测项分页查询，支持按节点ID过滤")
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('inspection:view')")
    public R<Page<InspectionItem>> page(@RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "10") int size,
                                         @RequestParam(required = false) Long nodeId) {
        return R.ok(inspectionItemService.pageQuery(page, size, nodeId));
    }

    @Operation(summary = "获取检测项详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('inspection:view')")
    public R<InspectionItem> getById(@PathVariable Long id) {
        return R.ok(inspectionItemService.getById(id));
    }

    @Operation(summary = "新增检测项", description = "创建检测项，同时根据配置动态注册 Quartz 定时任务")
    @PostMapping
    @PreAuthorize("hasAuthority('inspection:add')")
    public R<Long> create(@Valid @RequestBody InspectionItemDTO dto) {
        return saveOrUpdateItem(dto);
    }

    @Operation(summary = "更新检测项", description = "更新检测项，同时根据配置动态更新 Quartz 定时任务")
    @PutMapping
    @PreAuthorize("hasAuthority('inspection:edit')")
    public R<Long> update(@Valid @RequestBody InspectionItemDTO dto) {
        return saveOrUpdateItem(dto);
    }

    private R<Long> saveOrUpdateItem(InspectionItemDTO dto) {
        // 保存检测项
        inspectionItemService.saveOrUpdateItem(dto);
        
        // 获取保存后的ID (MyBatis-Plus 会自动回填 ID)
        Long itemId = dto.getId();
        
        // 获取完整检测项信息以判断是否需要创建定时任务
        InspectionItem item = inspectionItemService.getById(itemId);
        if (item == null) {
            return R.fail("保存检测项失败");
        }
        
        // 动态管理 Quartz 任务
        Integer isActive = item.getIsActive();
        Integer dataSource = item.getDataSource();
        String cronExpression = item.getCronExpression();
        
        if (isActive != null && isActive == 1 && 
            (dataSource == 0 || dataSource == 1) && 
            cronExpression != null && !cronExpression.isEmpty()) {
            quartzScheduleService.scheduleJob(itemId, cronExpression);
        } else {
            try {
                quartzScheduleService.deleteJob(itemId);
            } catch (Exception ignored) {
            }
        }
        
        return R.ok(itemId);
    }

    @Operation(summary = "启用/禁用检测项", description = "切换检测项的启用状态，同时管理关联的 Quartz 定时任务")
    @PostMapping("/{id}/toggle-active")
    @PreAuthorize("hasAuthority('inspection:edit')")
    public R<Void> toggleActive(@PathVariable Long id, @RequestBody java.util.Map<String, Boolean> body) {
        Boolean isActive = body.get("isActive");
        InspectionItem item = inspectionItemService.getById(id);
        if (item == null) {
            return R.fail("检测项不存在");
        }
        item.setIsActive(isActive != null && isActive ? 1 : 0);
        inspectionItemService.updateById(item);

        // 动态管理 Quartz 任务
        if (item.getIsActive() == 1 && item.getCronExpression() != null && !item.getCronExpression().isEmpty()) {
            quartzScheduleService.scheduleJob(id, item.getCronExpression());
        } else {
            try { quartzScheduleService.deleteJob(id); } catch (Exception ignored) {}
        }
        return R.ok();
    }

    @Operation(summary = "删除检测项", description = "删除检测项及关联的 Quartz 定时任务")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('inspection:delete')")
    public R<Void> delete(@PathVariable Long id) {
        // 先删除 Quartz 任务（忽略失败）
        try {
            quartzScheduleService.deleteJob(id);
        } catch (Exception ignored) {
            // 任务可能不存在
        }
        // 再删��检测项
        inspectionItemService.removeById(id);
        return R.ok();
    }

    @Operation(summary = "获取默认值分组", description = "按检测项ID查询默认值分组列表")
    @GetMapping("/default-value")
    public R<Object> defaultValueGroups(@RequestParam(required = false) Long itemId) {
        return R.ok(inspectionItemService.getDefaultValueGroups(itemId));
    }

    @Operation(summary = "创建默认值分组")
    @PostMapping("/default-value")
    public R<Void> createDefaultValueGroup(@RequestBody com.qims.domain.entity.InspectionDefaultValue data) {
        inspectionItemService.saveDefaultValueGroup(data);
        return R.ok();
    }

    @Operation(summary = "更新默认值分组")
    @PutMapping("/default-value")
    public R<Void> updateDefaultValueGroup(@RequestBody com.qims.domain.entity.InspectionDefaultValue data) {
        inspectionItemService.updateDefaultValueGroup(data);
        return R.ok();
    }

    @Operation(summary = "删除默认值分组")
    @DeleteMapping("/default-value")
    public R<Void> deleteDefaultValueGroup(@RequestParam String groupName) {
        inspectionItemService.deleteDefaultValueGroup(groupName);
        return R.ok();
    }
}
