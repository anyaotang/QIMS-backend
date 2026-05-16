package com.qims.web.controller;

import com.qims.common.result.R;
import com.qims.domain.mapper.DepartmentMapper;
import com.qims.domain.mapper.InspectionItemMapper;
import com.qims.domain.mapper.InspectionRecordMapper;
import com.qims.domain.mapper.NodeMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * 统计查询控制器
 */
@Tag(name = "11 统计查询", description = "综合统计数据：合格率、检测项数、记录数、节点数、部门数")
@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final InspectionItemMapper itemMapper;
    private final InspectionRecordMapper recordMapper;
    private final NodeMapper nodeMapper;
    private final DepartmentMapper departmentMapper;

    @Operation(summary = "综合统计", description = "返回合格率、检测项总数/启用数、今日记录数、不合格数、节点数、部门数")
    @GetMapping
    @PreAuthorize("hasAuthority('statistics:view')")
    public R<Map<String, Object>> statistics() {
        // 检测项统计
        long totalItems = itemMapper.countTotal();
        long activeItems = itemMapper.countActive();

        // 检测记录统计
        long todayRecords = recordMapper.countTodayRecords();
        long unqualifiedCount = recordMapper.countUnqualified();
        long allRecords = recordMapper.countAll();

        // 合格率计算：保留 1 位小数
        double qualifiedRate = 0.0;
        if (allRecords > 0) {
            qualifiedRate = BigDecimal.valueOf(allRecords - unqualifiedCount)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(allRecords), 1, RoundingMode.HALF_UP)
                    .doubleValue();
        }

        // 节点 & 部门统计
        long nodeCount = nodeMapper.countAll();
        long departmentCount = departmentMapper.countAll();

        // 组装返回
        Map<String, Object> result = new HashMap<>();
        result.put("totalItems", totalItems);
        result.put("activeItems", activeItems);
        result.put("qualifiedRate", qualifiedRate);
        result.put("todayRecords", todayRecords);
        result.put("unqualifiedCount", unqualifiedCount);
        result.put("nodeCount", nodeCount);
        result.put("departmentCount", departmentCount);

        return R.ok(result);
    }
}
