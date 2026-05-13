package com.qims.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qims.domain.entity.Department;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
}
