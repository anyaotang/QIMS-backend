package com.qims.web.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qims.common.result.R;
import com.qims.domain.entity.OperationLog;
import com.qims.service.service.OperationLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 操作日志控制器
 */
@Tag(name = "12 操作日志", description = "操作日志分页查询、模块列表")
@RestController
@RequestMapping("/api/log")
@RequiredArgsConstructor
public class LogController {

    private final OperationLogService operationLogService;

    @Operation(summary = "分页查询操作日志", description = "支持按模块和关键字过滤，关键字会匹配用户名、描述、URL、IP")
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('log:view')")
    public R<Page<OperationLog>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String keyword) {
        Page<OperationLog> result = operationLogService.pageQuery(page, size, module);
        // 如果传入了 keyword，做内存过滤（用户名/描述/URL 模糊匹配）
        if (keyword != null && !keyword.isBlank()) {
            String kw = keyword.trim().toLowerCase();
            result.setRecords(result.getRecords().stream()
                    .filter(log -> containsIgnoreCase(log.getUsername(), kw)
                            || containsIgnoreCase(log.getDescription(), kw)
                            || containsIgnoreCase(log.getUrl(), kw)
                            || containsIgnoreCase(log.getIp(), kw))
                    .collect(Collectors.toList()));
        }
        return R.ok(result);
    }

    @Operation(summary = "获取可选的模块列表", description = "返回系统已知的操作模块名称列表")
    @GetMapping("/modules")
    @PreAuthorize("hasAuthority('log:view')")
    public R<List<String>> modules() {
        // 从权限表中已知模块有: 用户管理, 部门管理, 角色管理, 节点管理, 检测项管理, 检测记录, 实施方案
        List<String> modules = Arrays.asList(
                "用户管理", "部门管理", "角色管理",
                "节点管理", "检测项管理", "检测记录",
                "实施方案", "登录认证", "系统管理"
        );
        return R.ok(modules);
    }

    private static boolean containsIgnoreCase(String source, String target) {
        if (source == null) return false;
        return source.toLowerCase().contains(target);
    }
}
