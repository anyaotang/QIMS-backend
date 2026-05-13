package com.qims.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qims.domain.entity.OperationLog;
import com.qims.domain.mapper.OperationLogMapper;
import com.qims.service.service.OperationLogService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 操作日志服务实现
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {

    @Override
    public Page<OperationLog> pageQuery(int page, int size, String module) {
        LambdaQueryWrapper<OperationLog> wrapper = new LambdaQueryWrapper<>();
        if (module != null && !module.isEmpty()) {
            wrapper.eq(OperationLog::getModule, module);
        }
        wrapper.orderByDesc(OperationLog::getCreateTime);
        return baseMapper.selectPage(new Page<>(page, size), wrapper);
    }

    @Override
    @Async
    public void saveLog(String username, String module, String description,
                        String method, String url, String ip,
                        String params, Object result, Long duration) {
        OperationLog log = new OperationLog();
        log.setUsername(username);
        log.setModule(module);
        log.setDescription(description);
        log.setMethod(method);
        log.setUrl(url);
        log.setIp(ip);
        log.setParams(params);
        log.setResult(result != null ? result.toString() : null);
        log.setDuration(duration);
        baseMapper.insert(log);
    }
}
