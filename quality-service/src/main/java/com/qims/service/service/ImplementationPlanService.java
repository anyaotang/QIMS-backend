package com.qims.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qims.domain.entity.ImplementationPlan;

import java.util.List;

/**
 * 实施方案服务接口
 */
public interface ImplementationPlanService extends IService<ImplementationPlan> {

    /**
     * 获取方案树
     */
    List<ImplementationPlan> getTree();
}
