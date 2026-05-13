package com.qims.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用状态枚举
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {

    ENABLE(1, "启用"),
    DISABLE(0, "禁用");

    private final Integer code;
    private final String desc;

    public static StatusEnum fromCode(Integer code) {
        for (StatusEnum s : values()) {
            if (s.code.equals(code)) {
                return s;
            }
        }
        throw new IllegalArgumentException("未知状态码: " + code);
    }
}
