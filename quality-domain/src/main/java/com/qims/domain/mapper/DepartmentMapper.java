package com.qims.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qims.domain.entity.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {

    /** 部门总数 */
    @Select("SELECT COUNT(*) FROM sys_department WHERE deleted = 0")
    long countAll();
}
