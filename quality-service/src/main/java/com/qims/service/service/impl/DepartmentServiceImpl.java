package com.qims.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qims.domain.entity.Department;
import com.qims.domain.mapper.DepartmentMapper;
import com.qims.service.service.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门服务实现
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Override
    public List<Department> getTree() {
        List<Department> all = baseMapper.selectList(
                new LambdaQueryWrapper<Department>().eq(Department::getStatus, 1).orderByAsc(Department::getSort)
        );
        return buildTree(all, 0L);
    }

    @Override
    public void setInheritRole(Long departmentId, List<Long> roleIds) {
        Department dept = baseMapper.selectById(departmentId);
        if (dept != null) {
            dept.setInheritRoleIds(roleIds.stream().map(String::valueOf).collect(Collectors.joining(",")));
            baseMapper.updateById(dept);
        }
    }

    private List<Department> buildTree(List<Department> all, Long parentId) {
        return all.stream()
                .filter(d -> parentId.equals(d.getParentId()))
                .peek(d -> d.setChildren(buildTree(all, d.getId())))
                .collect(Collectors.toList());
    }
}
