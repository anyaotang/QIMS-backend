package com.qims.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qims.domain.entity.InspectionItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface InspectionItemMapper extends BaseMapper<InspectionItem> {

    /** 检测项总数 */
    @Select("SELECT COUNT(*) FROM qims_inspection_item WHERE deleted = 0")
    long countTotal();

    /** 活跃（启用）检测项数 */
    @Select("SELECT COUNT(*) FROM qims_inspection_item WHERE deleted = 0 AND is_active = 1")
    long countActive();
}
