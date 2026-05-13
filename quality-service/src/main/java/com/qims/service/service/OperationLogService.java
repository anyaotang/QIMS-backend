package com.qims.service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qims.domain.entity.OperationLog;

/**
 * 操作日志服务接口
 */
public interface OperationLogService extends com.baomidou.mybatisplus.extension.service.IService<OperationLog> {

    /**
     * 分页查询日志
     */
    Page<OperationLog> pageQuery(int page, int size, String module);

    /**
     * 记录操作日志
     */
    void saveLog(String username, String module, String description,
                 String method, String url, String ip,
                 String params, Object result, Long duration);
}
