package com.qims.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qims.service.dto.ReportQueryDTO;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 报表服务接口
 */
public interface ReportService {

    /**
     * 分页查询质量报表
     */
    Page<?> queryQualityReport(ReportQueryDTO query);

    /**
     * 导出 Excel
     */
    void exportQualityReport(ReportQueryDTO query, HttpServletResponse response);
}
