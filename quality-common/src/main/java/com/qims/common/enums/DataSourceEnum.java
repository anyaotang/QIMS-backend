package com.qims.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 检测项数据来源枚举
 */
@Getter
@AllArgsConstructor
public enum DataSourceEnum {

    MANUAL(0, "手动录入"),
    API(1, "API自动采集"),
    FORMULA(2, "公式计算");

    private final Integer code;
    private final String desc;

    public static DataSourceEnum fromCode(Integer code) {
        for (DataSourceEnum s : values()) {
            if (s.code.equals(code)) {
                return s;
            }
        }
        throw new IllegalArgumentException("未知数据来源: " + code);
    }
}
