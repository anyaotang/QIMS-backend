package com.qims.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qims.domain.entity.ImplementationFeedback;
import com.qims.domain.mapper.ImplementationFeedbackMapper;
import com.qims.service.service.ImplementationFeedbackService;
import org.springframework.stereotype.Service;

/**
 * 实施方案反馈服务实现
 */
@Service
public class ImplementationFeedbackServiceImpl extends ServiceImpl<ImplementationFeedbackMapper, ImplementationFeedback> implements ImplementationFeedbackService {

    @Override
    public void submitFeedback(Long planId, String content) {
        ImplementationFeedback feedback = new ImplementationFeedback();
        feedback.setPlanId(planId);
        feedback.setContent(content);
        baseMapper.insert(feedback);
    }
}
