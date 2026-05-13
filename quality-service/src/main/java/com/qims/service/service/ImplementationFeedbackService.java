package com.qims.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qims.domain.entity.ImplementationFeedback;

/**
 * 实施方案反馈服务接口
 */
public interface ImplementationFeedbackService extends IService<ImplementationFeedback> {

    /**
     * 提交反馈
     */
    void submitFeedback(Long planId, String content);
}
