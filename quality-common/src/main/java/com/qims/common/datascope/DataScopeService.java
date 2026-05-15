package com.qims.common.datascope;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 数据权限工具类
 * <p>
 * 在 Service 层调用，自动根据当前用户的数据范围追加查询条件。
 * </p>
 *
 * <p>用法示例：</p>
 * <pre>
 *     LambdaQueryWrapper&lt;Node&gt; wrapper = new LambdaQueryWrapper&lt;&gt;();
 *     dataScopeService.applyDataScope(wrapper, Node::getDepartmentId);
 * </pre>
 */
@Slf4j
@Component
public class DataScopeService {

    /**
     * 数据范围枚举
     */
    public static final int SCOPE_ALL = 1;          // 全部数据
    public static final int SCOPE_DEPT = 2;          // 本部门
    public static final int SCOPE_DEPT_AND_CHILD = 3; // 本部门及子部门
    public static final int SCOPE_SELF = 4;          // 仅本人

    /**
     * 获取当前数据范围
     */
    public int getCurrentDataScope() {
        return DataScopeContext.getDataScope();
    }

    /**
     * 获取当前用户ID
     */
    public Long getCurrentUserId() {
        return DataScopeContext.getUserId();
    }

    /**
     * 获取当前用户部门ID
     */
    public Long getCurrentDeptId() {
        return DataScopeContext.getDeptId();
    }

    /**
     * 是否拥有全部数据权限
     */
    public boolean hasAllScope() {
        return getCurrentDataScope() == SCOPE_ALL;
    }

    /**
     * 对部门字段应用数据范围过滤
     * <p>
     * 根据当前用户的数据范围，自动在 wrapper 上追加部门ID过滤条件。
     * </p>
     *
     * @param wrapper     查询条件包装器
     * @param deptGetter  部门ID字段引用（如 Node::getDepartmentId）
     * @param <T>         实体类型
     */
    public <T> void applyDeptScope(LambdaQueryWrapper<T> wrapper,
                                    SFunction<T, Long> deptGetter) {
        Integer scope = getCurrentDataScope();
        Long deptId = getCurrentDeptId();

        if (scope == SCOPE_ALL) {
            return; // 全部数据，不加条件
        }

        if (scope == SCOPE_DEPT || scope == SCOPE_DEPT_AND_CHILD) {
            if (deptId != null) {
                wrapper.eq(deptGetter, deptId);
            }
        }
        // scope == SCOPE_SELF 在调用处单独处理（用 applyUserScope）
    }

    /**
     * 对用户字段应用数据范围过滤（仅本人）
     *
     * @param wrapper    查询条件包装器
     * @param userGetter 用户ID字段引用（如 User::getId）
     * @param <T>        实体类型
     */
    public <T> void applyUserScope(LambdaQueryWrapper<T> wrapper,
                                    SFunction<T, Long> userGetter) {
        Integer scope = getCurrentDataScope();
        Long userId = getCurrentUserId();

        if (scope == SCOPE_ALL || scope == SCOPE_DEPT || scope == SCOPE_DEPT_AND_CHILD) {
            return;
        }

        if (scope == SCOPE_SELF && userId != null) {
            wrapper.eq(userGetter, userId);
        }
    }
}
