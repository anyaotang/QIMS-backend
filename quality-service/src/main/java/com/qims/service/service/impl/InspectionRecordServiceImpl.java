package com.qims.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qims.common.exception.BizException;
import com.qims.domain.entity.InspectionItem;
import com.qims.domain.entity.InspectionRecord;
import com.qims.domain.mapper.InspectionItemMapper;
import com.qims.domain.mapper.InspectionRecordMapper;
import com.qims.service.dto.RecordManualDTO;
import com.qims.service.service.InspectionRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 检测记录服务实现
 */
@Service
@RequiredArgsConstructor
public class InspectionRecordServiceImpl extends ServiceImpl<InspectionRecordMapper, InspectionRecord> implements InspectionRecordService {

    private final InspectionItemMapper itemMapper;

    @Override
    public void manualRecord(RecordManualDTO dto) {
        InspectionItem item = itemMapper.selectById(dto.getItemId());
        if (item == null) {
            throw new BizException("检测项不存在");
        }

        InspectionRecord record = new InspectionRecord();
        record.setItemId(dto.getItemId());
        record.setValue(dto.getValue());
        record.setInspectTime(LocalDateTime.now());
        record.setDataSource(0); // 手动录入
        record.setRemark(dto.getRemark());

        // 判断是否达标
        boolean qualified = isQualified(dto.getValue(), item.getTargetValue());
        record.setIsQualified(qualified ? 1 : 0);

        baseMapper.insert(record);

        // TODO: 检查推送条件，匹配则发送邮件
    }

    @Override
    public List<InspectionRecord> listByItemId(Long itemId) {
        return baseMapper.selectList(
                new LambdaQueryWrapper<InspectionRecord>()
                        .eq(InspectionRecord::getItemId, itemId)
                        .orderByDesc(InspectionRecord::getInspectTime)
        );
    }

    /**
     * 简单达标判断：value <= targetValue 视为达标
     * 可根据业务需求扩展（范围判断、公式判断等）
     */
    private boolean isQualified(BigDecimal value, String targetValue) {
        if (value == null || targetValue == null || targetValue.isEmpty()) {
            return true;
        }
        try {
            BigDecimal target = new BigDecimal(targetValue);
            return value.compareTo(target) <= 0;
        } catch (NumberFormatException e) {
            return true;
        }
    }
}
