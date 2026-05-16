package com.qims.web.controller;

import com.qims.common.result.R;
import com.qims.service.dto.ReportQueryDTO;
import com.qims.service.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 报表控制器
 */
@Tag(name = "10 报表管理", description = "质量报表条件查询、Excel 导出")
@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "条件查询质量报表（POST）", description = "通过请求体传递复杂查询条件")
    @PostMapping("/quality")
    @PreAuthorize("hasAuthority('report:view')")
    public R<?> queryQuality(@RequestBody ReportQueryDTO query) {
        return R.ok(reportService.queryQualityReport(query));
    }

    @Operation(summary = "条件查询质量报表（GET）", description = "通过 URL 参数传递简单查询条件")
    @GetMapping("/quality")
    @PreAuthorize("hasAuthority('report:view')")
    public R<?> queryQualityGet(ReportQueryDTO query) {
        return R.ok(reportService.queryQualityReport(query));
    }

    @Operation(summary = "导出质量报表 Excel", description = "根据查询条件导出 Excel 文件")
    @PostMapping("/quality/export")
    @PreAuthorize("hasAuthority('report:export')")
    public void exportQuality(@RequestBody ReportQueryDTO query, HttpServletResponse response) {
        reportService.exportQualityReport(query, response);
    }
}
