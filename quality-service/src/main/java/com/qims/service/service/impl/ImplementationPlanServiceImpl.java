package com.qims.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qims.domain.entity.ImplementationPlan;
import com.qims.domain.mapper.ImplementationPlanMapper;
import com.qims.service.service.ImplementationPlanService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 实施方案服务实现
 */
@Service
public class ImplementationPlanServiceImpl extends ServiceImpl<ImplementationPlanMapper, ImplementationPlan> implements ImplementationPlanService {

    @Override
    public List<ImplementationPlan> getTree() {
        List<ImplementationPlan> all = baseMapper.selectList(
                new LambdaQueryWrapper<ImplementationPlan>().orderByAsc(ImplementationPlan::getDeadline)
        );
        return buildTree(all, 0L);
    }

    private List<ImplementationPlan> buildTree(List<ImplementationPlan> all, Long parentId) {
        return all.stream()
                .filter(p -> parentId.equals(p.getParentId()))
                .peek(p -> p.setChildren(buildTree(all, p.getId())))
                .collect(Collectors.toList());
    }
}
