package com.qims.web.controller;

import com.qims.common.result.R;
import com.qims.service.dto.ReportQueryDTO;
import com.qims.service.service.ReportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 报表控制器
 */
@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    /**
     * 条件查询质量报表（POST - 前端复杂查询用 body）
     */
    @PostMapping("/quality")
    public R<?> queryQuality(@RequestBody ReportQueryDTO query) {
        return R.ok(reportService.queryQualityReport(query));
    }

    /**
     * 条件查询质量报表（GET - 前端简单查询用 params）
     */
    @GetMapping("/quality")
    public R<?> queryQualityGet(ReportQueryDTO query) {
        return R.ok(reportService.queryQualityReport(query));
    }

    /**
     * 导出 Excel
     */
    @PostMapping("/quality/export")
    public void exportQuality(@RequestBody ReportQueryDTO query, HttpServletResponse response) {
        reportService.exportQualityReport(query, response);
    }
}
