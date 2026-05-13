package com.qims.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qims.domain.entity.Department;

import java.util.List;

/**
 * 部门服务接口
 */
public interface DepartmentService extends IService<Department> {

    /**
     * 获取部门树
     */
    List<Department> getTree();

    /**
     * 设置部门继承角色
     */
    void setInheritRole(Long departmentId, List<Long> roleIds);
}
