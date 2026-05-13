package com.qims.web.controller;

import com.qims.common.result.R;
import com.qims.service.service.OperationLogService;
import com.qims.service.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 统计查询控制器
 */
@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final ReportService reportService;
    private final OperationLogService logService;

    /**
     * 综合统计（合格率、节点数、检测项数、记录数）
     */
    @GetMapping
    public R<Map<String, Object>> statistics() {
        Map<String, Object> result = new HashMap<>();
        // TODO: 补充具体统计 SQL / 调用 Service 方法
        result.put("message", "统计功能待完善");
        return R.ok(result);
    }
}
