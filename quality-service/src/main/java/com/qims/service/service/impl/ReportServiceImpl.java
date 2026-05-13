package com.qims.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qims.domain.entity.InspectionRecord;
import com.qims.domain.entity.Node;
import com.qims.domain.entity.InspectionItem;
import com.qims.domain.mapper.InspectionRecordMapper;
import com.qims.domain.mapper.InspectionItemMapper;
import com.qims.domain.mapper.NodeMapper;
import com.qims.service.dto.ReportQueryDTO;
import com.qims.service.service.ReportService;
import com.qims.service.service.OperationLogService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报表服务实现
 */
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final InspectionRecordMapper recordMapper;
    private final InspectionItemMapper itemMapper;
    private final NodeMapper nodeMapper;
    private final OperationLogService logService;

    @Override
    public Page<?> queryQualityReport(ReportQueryDTO query) {
        Page<InspectionRecord> page = new Page<>(query.getPage(), query.getPageSize());
        LambdaQueryWrapper<InspectionRecord> wrapper = new LambdaQueryWrapper<>();

        if (query.getItemId() != null) {
            wrapper.eq(InspectionRecord::getItemId, query.getItemId());
        }
        if (query.getIsQualified() != null) {
            wrapper.eq(InspectionRecord::getIsQualified, query.getIsQualified());
        }
        if (query.getStartDate() != null && !query.getStartDate().isBlank()) {
            wrapper.ge(InspectionRecord::getInspectTime, LocalDateTime.parse(query.getStartDate()));
        }
        if (query.getEndDate() != null && !query.getEndDate().isBlank()) {
            wrapper.le(InspectionRecord::getInspectTime, LocalDateTime.parse(query.getEndDate()));
        }
        wrapper.orderByDesc(InspectionRecord::getInspectTime);

        return recordMapper.selectPage(page, wrapper);
    }

    @Override
    public void exportQualityReport(ReportQueryDTO query, HttpServletResponse response) {
        // 查询数据
        Page<InspectionRecord> page = new Page<>(1, 10000);
        LambdaQueryWrapper<InspectionRecord> wrapper = new LambdaQueryWrapper<>();
        if (query.getItemId() != null) {
            wrapper.eq(InspectionRecord::getItemId, query.getItemId());
        }
        if (query.getIsQualified() != null) {
            wrapper.eq(InspectionRecord::getIsQualified, query.getIsQualified());
        }
        wrapper.orderByDesc(InspectionRecord::getInspectTime);
        List<InspectionRecord> records = recordMapper.selectPage(page, wrapper).getRecords();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("质量报表");

            // 表头
            Row headerRow = sheet.createRow(0);
            String[] headers = {"检测项", "节点", "检测值", "目标值", "是否达标", "检测时间", "数据来源", "备注"};
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // 数据行
            CellStyle qualifiedStyle = workbook.createCellStyle();
            qualifiedStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            qualifiedStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle unqualifiedStyle = workbook.createCellStyle();
            unqualifiedStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
            unqualifiedStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            int rowNum = 1;
            for (InspectionRecord record : records) {
                InspectionItem item = itemMapper.selectById(record.getItemId());

                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(item != null ? item.getName() : "");
                if (item != null && item.getNodeId() != null) {
                    Node node = nodeMapper.selectById(item.getNodeId());
                    row.createCell(1).setCellValue(node != null ? node.getName() : "");
                } else {
                    row.createCell(1).setCellValue("");
                }
                row.createCell(2).setCellValue(record.getValue() != null ? record.getValue().toString() : "");
                row.createCell(3).setCellValue(item != null ? item.getTargetValue() : "");
                row.createCell(4).setCellValue(record.getIsQualified() == 1 ? "达标" : "不达标");
                row.createCell(5).setCellValue(record.getInspectTime() != null ?
                        record.getInspectTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "");
                row.createCell(6).setCellValue(getDataSourceName(record.getDataSource()));
                row.createCell(7).setCellValue(record.getRemark() != null ? record.getRemark() : "");

                // 报表变色
                Cell statusCell = row.getCell(4);
                statusCell.setCellStyle(record.getIsQualified() == 1 ? qualifiedStyle : unqualifiedStyle);
            }

            // 输出
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" +
                    URLEncoder.encode("质量报表_" + System.currentTimeMillis() + ".xlsx", StandardCharsets.UTF_8));
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException("导出失败", e);
        }
    }

    private String getDataSourceName(Integer dataSource) {
        if (dataSource == null) return "未知";
        return switch (dataSource) {
            case 0 -> "手动";
            case 1 -> "API";
            case 2 -> "公式";
            case 3 -> "定时采集";
            default -> "未知";
        };
    }
}
