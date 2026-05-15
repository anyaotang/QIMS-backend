package com.qims.common.datascope;

/**
 * 数据权限上下文（ThreadLocal）
 * <p>
 * 在 JWT 过滤器中设置当前用户的 userId 和 departmentId，
 * 在 MyBatis Plus 拦截器中读取并追加数据范围 SQL。
 * </p>
 */
public class DataScopeContext {

    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<Long> DEPT_ID = new ThreadLocal<>();
    private static final ThreadLocal<Integer> DATA_SCOPE = new ThreadLocal<>();

    public static void set(Long userId, Long deptId, Integer dataScope) {
        USER_ID.set(userId);
        DEPT_ID.set(deptId);
        DATA_SCOPE.set(dataScope);
    }

    public static Long getUserId() {
        return USER_ID.get();
    }

    public static Long getDeptId() {
        return DEPT_ID.get();
    }

    public static Integer getDataScope() {
        // 默认仅本人
        Integer scope = DATA_SCOPE.get();
        return scope != null ? scope : 4;
    }

    public static void clear() {
        USER_ID.remove();
        DEPT_ID.remove();
        DATA_SCOPE.remove();
    }
}
