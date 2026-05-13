package com.qims.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 节点类型枚举
 */
@Getter
@AllArgsConstructor
public enum NodeTypeEnum {

    DEPARTMENT(0, "部门"),
    PRODUCT(1, "产品"),
    PROCESS(2, "工序"),
    INSPECTION_POINT(3, "检测点");

    private final Integer code;
    private final String desc;

    public static NodeTypeEnum fromCode(Integer code) {
        for (NodeTypeEnum s : values()) {
            if (s.code.equals(code)) {
                return s;
            }
        }
        throw new IllegalArgumentException("未知节点类型: " + code);
    }
}
