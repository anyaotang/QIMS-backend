package com.qims.common.datascope;

import java.lang.annotation.*;

/**
 * 数据权限注解
 * <p>
 * 标注在 Controller 或 Service 方法上，用于标记该方法需要进行数据权限过滤。
 * 配合 {@link DataScopeAspect} 使用。
 * </p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {
    /**
     * 部门表/实体中的部门ID字段名
     * 默认 "department_id"
     */
    String deptIdField() default "department_id";
}
