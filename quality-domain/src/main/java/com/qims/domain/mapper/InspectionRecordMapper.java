package com.qims.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qims.domain.entity.InspectionRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface InspectionRecordMapper extends BaseMapper<InspectionRecord> {

    /** 今日检测记录数 */
    @Select("SELECT COUNT(*) FROM qims_inspection_record WHERE deleted = 0 AND DATE(inspect_time) = CURDATE()")
    long countTodayRecords();

    /** 全部不合格记录数 */
    @Select("SELECT COUNT(*) FROM qims_inspection_record WHERE deleted = 0 AND is_qualified = 0")
    long countUnqualified();

    /** 总记录数（用于计算合格率） */
    @Select("SELECT COUNT(*) FROM qims_inspection_record WHERE deleted = 0")
    long countAll();
}
