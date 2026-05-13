package com.qims.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qims.domain.entity.InspectionRecord;
import com.qims.service.dto.RecordManualDTO;

/**
 * 检测记录服务接口
 */
public interface InspectionRecordService extends IService<InspectionRecord> {

    /**
     * 手动录入检测记录
     */
    void manualRecord(RecordManualDTO dto);

    /**
     * 根据检测项ID查询记录列表
     */
    java.util.List<InspectionRecord> listByItemId(Long itemId);
}
