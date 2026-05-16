package com.qims.web.controller;

import com.qims.common.result.R;
import com.qims.domain.entity.ImplementationPlan;
import com.qims.service.dto.FeedbackDTO;
import com.qims.service.service.ImplementationFeedbackService;
import com.qims.service.service.ImplementationPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 实施方案控制器
 */
@Tag(name = "09 实施方案", description = "实施方案 CRUD、方案树、反馈管理")
@RestController
@RequestMapping("/api/implementation")
@RequiredArgsConstructor
public class ImplementationPlanController {

    private final ImplementationPlanService planService;
    private final ImplementationFeedbackService feedbackService;

    @Operation(summary = "获取方案树", description = "返回树形结构的实施方案列表")
    @GetMapping("/plan/tree")
    @PreAuthorize("hasAuthority('plan:view')")
    public R<List<ImplementationPlan>> getPlanTree() {
        return R.ok(planService.getTree());
    }

    @Operation(summary = "新增方案")
    @PostMapping("/plan")
    @PreAuthorize("hasAuthority('plan:add')")
    public R<Void> savePlan(@RequestBody ImplementationPlan plan) {
        planService.save(plan);
        return R.ok();
    }

    @Operation(summary = "更新方案")
    @PutMapping("/plan")
    @PreAuthorize("hasAuthority('plan:edit')")
    public R<Void> updatePlan(@RequestBody ImplementationPlan plan) {
        planService.updateById(plan);
        return R.ok();
    }

    @Operation(summary = "删除方案")
    @DeleteMapping("/plan/{id}")
    @PreAuthorize("hasAuthority('plan:delete')")
    public R<Void> deletePlan(@Parameter(description = "方案ID") @PathVariable Long id) {
        planService.removeById(id);
        return R.ok();
    }

    @Operation(summary = "提交反馈", description = "为指定方案提交反馈内容")
    @PostMapping("/feedback")
    @PreAuthorize("hasAuthority('feedback:add')")
    public R<Void> submitFeedback(@RequestBody FeedbackDTO dto) {
        feedbackService.submitFeedback(dto.getPlanId(), dto.getContent());
        return R.ok();
    }
}
