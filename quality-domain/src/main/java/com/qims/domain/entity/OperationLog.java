package com.qims.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qims.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 操作日志实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_operation_log")
public class OperationLog extends BaseEntity {

    /** 操作用户 */
    private String username;

    /** 操作模块 */
    private String module;

    /** 操作描述 */
    private String description;

    /** 请求方法 */
    private String method;

    /** 请求URL */
    private String url;

    /** 请求IP */
    private String ip;

    /** 请求参数 */
    private String params;

    /** 返回结果 */
    private String result;

    /** 耗时(ms) */
    private Long duration;
}
