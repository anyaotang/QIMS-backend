package com.qims.web.controller;

import com.qims.common.result.R;
import com.qims.domain.entity.ImplementationPlan;
import com.qims.service.dto.FeedbackDTO;
import com.qims.service.service.ImplementationFeedbackService;
import com.qims.service.service.ImplementationPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 实施方案控制器
 */
@RestController
@RequestMapping("/api/implementation")
@RequiredArgsConstructor
public class ImplementationPlanController {

    private final ImplementationPlanService planService;
    private final ImplementationFeedbackService feedbackService;

    /**
     * 获取方案树
     */
    @GetMapping("/plan/tree")
    public R<List<ImplementationPlan>> getPlanTree() {
        return R.ok(planService.getTree());
    }

    /**
     * 新增方案
     */
    @PostMapping("/plan")
    public R<Void> savePlan(@RequestBody ImplementationPlan plan) {
        planService.save(plan);
        return R.ok();
    }

    /**
     * 更新方案
     */
    @PutMapping("/plan")
    public R<Void> updatePlan(@RequestBody ImplementationPlan plan) {
        planService.updateById(plan);
        return R.ok();
    }

    /**
     * 删除方案
     */
    @DeleteMapping("/plan/{id}")
    public R<Void> deletePlan(@PathVariable Long id) {
        planService.removeById(id);
        return R.ok();
    }

    /**
     * 提交反馈
     */
    @PostMapping("/feedback")
    public R<Void> submitFeedback(@RequestBody FeedbackDTO dto) {
        feedbackService.submitFeedback(dto.getPlanId(), dto.getContent());
        return R.ok();
    }
}
