package com.qims.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qims.domain.entity.Node;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface NodeMapper extends BaseMapper<Node> {

    /** 节点总数 */
    @Select("SELECT COUNT(*) FROM qims_node WHERE deleted = 0")
    long countAll();
}
