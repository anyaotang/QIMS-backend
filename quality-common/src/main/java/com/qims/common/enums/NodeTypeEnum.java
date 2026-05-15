package com.qims.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 节点类型枚举（对应 qims_node.type 字段）
 */
@Getter
@AllArgsConstructor
public enum NodeTypeEnum {

    FACTORY("工厂", "工厂"),
    WORKSHOP("车间", "车间"),
    PRODUCTION_LINE("生产线", "生产线"),
    EQUIPMENT("设备", "设备"),
    INSPECTION_STATION("检测工位", "检测工位");

    private final String code;
    private final String desc;

    public static NodeTypeEnum fromCode(String code) {
        for (NodeTypeEnum s : values()) {
            if (s.code.equals(code)) {
                return s;
            }
        }
        throw new IllegalArgumentException("未知节点类型: " + code);
    }
}
