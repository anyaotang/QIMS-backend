package com.qims.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qims.common.result.R;
import com.qims.domain.entity.InspectionRecord;
import com.qims.service.dto.RecordManualDTO;
import com.qims.service.service.InspectionRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 检测记录控制器
 */
@Tag(name = "07 检测记录", description = "检测记录查询、手动录入、删除")
@RestController
@RequestMapping("/api/record")
@RequiredArgsConstructor
public class InspectionRecordController {

    private final InspectionRecordService recordService;

    @Operation(summary = "分页查询检测记录", description = "按检测项ID、合格状态、数据来源等条件分页查询")
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('record:manual')")
    public R<Page<InspectionRecord>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long itemId,
            @RequestParam(required = false) Integer isQualified,
            @RequestParam(required = false) Integer dataSource) {
        LambdaQueryWrapper<InspectionRecord> wrapper = new LambdaQueryWrapper<>();
        if (itemId != null) {
            wrapper.eq(InspectionRecord::getItemId, itemId);
        }
        if (isQualified != null) {
            wrapper.eq(InspectionRecord::getIsQualified, isQualified);
        }
        if (dataSource != null) {
            wrapper.eq(InspectionRecord::getDataSource, dataSource);
        }
        wrapper.orderByDesc(InspectionRecord::getInspectTime);
        return R.ok(recordService.page(new Page<>(page, size), wrapper));
    }

    @Operation(summary = "手动录入检测记录")
    @PostMapping("/manual")
    @PreAuthorize("hasAuthority('record:add')")
    public R<Void> manualRecord(@RequestBody RecordManualDTO dto) {
        recordService.manualRecord(dto);
        return R.ok();
    }

    @Operation(summary = "根据检测项ID查询记录列表")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('record:manual')")
    public R<List<?>> listByItemId(@RequestParam Long itemId) {
        return R.ok(recordService.listByItemId(itemId));
    }

    @Operation(summary = "获取记录详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('record:manual')")
    public R<InspectionRecord> getById(@PathVariable Long id) {
        return R.ok(recordService.getById(id));
    }

    @Operation(summary = "删除记录")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('record:modify')")
    public R<Void> delete(@PathVariable Long id) {
        recordService.removeById(id);
        return R.ok();
    }
}
