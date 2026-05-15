-- =====================================================
-- QIMS 质量检测管理系统 - 数据库初始化脚本
-- 数据库: MySQL 8.0
-- 字符集: utf8mb4
-- =====================================================

CREATE DATABASE IF NOT EXISTS qims
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;

USE qims;

-- =====================================================
-- 1. 部门表
-- =====================================================
CREATE TABLE IF NOT EXISTS sys_department (
    id          BIGINT NOT NULL PRIMARY KEY COMMENT '部门ID(雪花算法)',
    name        VARCHAR(100)  NOT NULL COMMENT '部门名称',
    parent_id   BIGINT       NOT NULL DEFAULT 0 COMMENT '父部门ID，0 为根',
    inherit_role_ids VARCHAR(500) NULL COMMENT '继承角色ID，逗号分隔',
    sort        INT          NOT NULL DEFAULT 0 COMMENT '排序',
    status      INT          NOT NULL DEFAULT 1 COMMENT '1-启用 0-禁用',
    deleted     INT          NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_parent (parent_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- =====================================================
-- 2. 用户表
-- =====================================================
CREATE TABLE IF NOT EXISTS sys_user (
    id              BIGINT NOT NULL PRIMARY KEY COMMENT '用户ID(雪花算法)',
    username        VARCHAR(50)  NOT NULL UNIQUE COMMENT '登录用户名',
    password        VARCHAR(255) NOT NULL COMMENT 'BCrypt加密密码',
    real_name       VARCHAR(100) NULL COMMENT '真实姓名',
    email           VARCHAR(100) NULL COMMENT '邮箱',
    phone           VARCHAR(20)  NULL COMMENT '手机号',
    department_id   BIGINT       NULL COMMENT '所属部门ID',
    status          INT          NOT NULL DEFAULT 1 COMMENT '1-启用 0-禁用',
    deleted         INT          NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_department (department_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- =====================================================
-- 3. 角色表
-- =====================================================
CREATE TABLE IF NOT EXISTS sys_role (
    id          BIGINT NOT NULL PRIMARY KEY COMMENT '角色ID(雪花算法)',
    name        VARCHAR(100) NOT NULL COMMENT '角色名称',
    code        VARCHAR(100) NOT NULL UNIQUE COMMENT '角色编码',
    description VARCHAR(255) NULL COMMENT '描述',
    remark      VARCHAR(255) NULL COMMENT '备注',
    status      INT         NOT NULL DEFAULT 1 COMMENT '1-启用 0-禁用',
    data_scope  INT         NOT NULL DEFAULT 1 COMMENT '数据范围(1全部2本部门3本部门及子部门4仅本人)',
    deleted     INT         NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    create_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- =====================================================
-- 4. 权限表（支持菜单树结构）
-- =====================================================
CREATE TABLE IF NOT EXISTS sys_permission (
    id          BIGINT NOT NULL PRIMARY KEY COMMENT '权限ID(雪花算法)',
    name        VARCHAR(100) NOT NULL COMMENT '权限名称',
    code        VARCHAR(100) NOT NULL UNIQUE COMMENT '权限编码，如 node:edit',
    type        INT         NOT NULL DEFAULT 1 COMMENT '类型(1目录2菜单3按钮)',
    parent_id   BIGINT       DEFAULT NULL COMMENT '父级ID',
    description VARCHAR(255) NULL COMMENT '描述',
    status      INT         NOT NULL DEFAULT 1 COMMENT '1-启用 0-禁用',
    deleted     INT         NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    create_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    path        VARCHAR(255) NULL COMMENT '路由路径',
    icon        VARCHAR(100) NULL COMMENT '图标',
    sort        INT         NOT NULL DEFAULT 0 COMMENT '排序',
    component   VARCHAR(255) NULL COMMENT '前端组件路径',
    INDEX idx_code (code),
    INDEX idx_parent (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- =====================================================
-- 5. 用户-角色关联表
-- =====================================================
CREATE TABLE IF NOT EXISTS sys_user_role (
    id          BIGINT NOT NULL PRIMARY KEY COMMENT '用户角色关联ID(雪花算法)',
    user_id     BIGINT NOT NULL,
    role_id     BIGINT NOT NULL,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted     INT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    UNIQUE KEY uk_user_role (user_id, role_id),
    INDEX idx_user (user_id),
    INDEX idx_role (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- =====================================================
-- 6. 角色-权限关联表
-- =====================================================
CREATE TABLE IF NOT EXISTS sys_role_permission (
    id            BIGINT NOT NULL PRIMARY KEY COMMENT '角色权限关联ID(雪花算法)',
    role_id       BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    create_time   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted       INT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    UNIQUE KEY uk_role_perm (role_id, permission_id),
    INDEX idx_role (role_id),
    INDEX idx_perm (permission_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- =====================================================
-- 7. 节点表
-- =====================================================
CREATE TABLE IF NOT EXISTS qims_node (
    id          BIGINT NOT NULL PRIMARY KEY COMMENT '节点ID(雪花算法)',
    name        VARCHAR(100)  NOT NULL COMMENT '节点名称',
    type        VARCHAR(50)   NOT NULL COMMENT '节点类型(工厂/车间/生产线/设备/检测工位)',
    parent_id   BIGINT        NOT NULL DEFAULT 0 COMMENT '父节点ID，0 为根',
    sort        INT            NOT NULL DEFAULT 0 COMMENT '排序',
    status      INT            NOT NULL DEFAULT 1 COMMENT '1-启用 0-禁用',
    description VARCHAR(500)  NULL COMMENT '描述',
    deleted     INT            NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    create_time DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_parent (parent_id),
    INDEX idx_type (type),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='检测节点表';

-- =====================================================
-- 8. 检测项表
-- =====================================================
CREATE TABLE IF NOT EXISTS qims_inspection_item (
    id                BIGINT NOT NULL PRIMARY KEY COMMENT '检测项ID(雪花算法)',
    name              VARCHAR(200)  NOT NULL COMMENT '检测项名称',
    node_id           BIGINT        NULL COMMENT '关联节点ID',
    data_source       INT           NOT NULL DEFAULT 0 COMMENT '数据来源：0-手动 1-API 2-公式',
    target_value      VARCHAR(100)  NULL COMMENT '目标值',
    unit              VARCHAR(50)   NULL COMMENT '单位',
    is_active         INT           NOT NULL DEFAULT 1 COMMENT '1-启用 0-禁用',
    cron_expression   VARCHAR(200)  NULL COMMENT 'Cron表达式（定时采集）',
    api_url           VARCHAR(500)  NULL COMMENT 'API地址（dataSource=1）',
    formula           TEXT          NULL COMMENT '公式表达式（dataSource=2）',
    push_condition    TEXT          NULL COMMENT '推送条件（Aviator表达式）',
    push_emails       VARCHAR(500)  NULL COMMENT '推送邮箱，逗号分隔',
    deleted           INT           NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    create_time       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_node (node_id),
    INDEX idx_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='检测项表';

-- =====================================================
-- 9. 检测记录表
-- =====================================================
CREATE TABLE IF NOT EXISTS qims_inspection_record (
    id             BIGINT NOT NULL PRIMARY KEY COMMENT '检测记录ID(雪花算法)',
    item_id        BIGINT        NOT NULL COMMENT '检测项ID',
    value          DECIMAL(18,4) NULL COMMENT '检测值',
    is_qualified   INT           NOT NULL DEFAULT 1 COMMENT '1-达标 0-不达标',
    inspect_time   DATETIME      NOT NULL COMMENT '检测时间',
    data_source    INT           NOT NULL DEFAULT 0 COMMENT '来源：0-手动 1-API 2-公式 3-定时',
    remark         VARCHAR(500)  NULL COMMENT '备注',
    deleted        INT           NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    create_time    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_item (item_id),
    INDEX idx_time (inspect_time),
    INDEX idx_qualified (is_qualified)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='检测记录表';

-- =====================================================
-- 10. 检测项默认值分组表
-- =====================================================
CREATE TABLE IF NOT EXISTS qims_inspection_default_value (
    id          BIGINT NOT NULL PRIMARY KEY COMMENT '检测项默认值ID(雪花算法)',
    item_id     BIGINT       NOT NULL COMMENT '检测项ID',
    group_name  VARCHAR(100) NOT NULL COMMENT '分组名称',
    default_value VARCHAR(500) NULL COMMENT '默认值',
    deleted     INT          NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_item (item_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='检测项默认值表';

-- =====================================================
-- 11. 实施方案表
-- =====================================================
CREATE TABLE IF NOT EXISTS qims_implementation_plan (
    id          BIGINT NOT NULL PRIMARY KEY COMMENT '实施方案ID(雪花算法)',
    name        VARCHAR(200)  NOT NULL COMMENT '方案名称',
    parent_id   BIGINT        NOT NULL DEFAULT 0 COMMENT '父方案ID，0 为根',
    responsible  VARCHAR(100)  NULL COMMENT '责任人',
    deadline    DATE           NULL COMMENT '截止日期',
    status      INT            NOT NULL DEFAULT 0 COMMENT '0-未开始 1-进行中 2-已完成',
    description TEXT           NULL COMMENT '描述',
    deleted     INT            NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    create_time DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_parent (parent_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='实施方案表';

-- =====================================================
-- 12. 实施方案反馈表
-- =====================================================
CREATE TABLE IF NOT EXISTS qims_implementation_feedback (
    id          BIGINT NOT NULL PRIMARY KEY COMMENT '实施反馈ID(雪花算法)',
    plan_id     BIGINT       NOT NULL COMMENT '方案ID',
    feedback_by VARCHAR(100) NULL COMMENT '反馈人',
    content     TEXT         NOT NULL COMMENT '反馈内容',
    deleted     INT         NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_plan (plan_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='实施方案反馈表';

-- =====================================================
-- 13. 操作日志表
-- =====================================================
CREATE TABLE IF NOT EXISTS sys_operation_log (
    id          BIGINT NOT NULL PRIMARY KEY COMMENT '日志ID(雪花算法)',
    username    VARCHAR(50)   NULL COMMENT '操作用户',
    module      VARCHAR(100)  NULL COMMENT '操作模块',
    description VARCHAR(255)  NULL COMMENT '操作描述',
    method      VARCHAR(200)  NULL COMMENT '请求方法',
    url         VARCHAR(500)  NULL COMMENT '请求URL',
    ip          VARCHAR(50)   NULL COMMENT '请求IP',
    params      TEXT          NULL COMMENT '请求参数',
    result      TEXT          NULL COMMENT '返回结果',
    duration    BIGINT        NULL COMMENT '耗时(ms)',
    deleted     INT           NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    create_time DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_module (module),
    INDEX idx_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- =====================================================
-- 14. 初始化数据：默认角色和权限树
-- =====================================================

-- 默认角色
INSERT INTO sys_role (id, name, code, remark, status, data_scope) VALUES
(313466522431393793, '管理员',         'ROLE_ADMIN',           '系统管理员，拥有所有权限', 1, 1),
(313466522431393794, '质量经理',       'ROLE_QUALITY_MANAGER', '质量管理负责人',          1, 2),
(313466522431393795, '质检员',         'ROLE_INSPECTOR',       '质量检验员',             1, 4),
(313466522431393796, '生产主管',       'ROLE_PRODUCTION_MANAGER','生产管理负责人',         1, 2),
(313466522431393797, '一线员工',       'ROLE_WORKER',          '一线操作员工',           1, 4)
ON DUPLICATE KEY UPDATE name=name;

-- 权限树数据（type: 1=目录 2=菜单 3=按钮）
INSERT INTO sys_permission (id, name, code, type, parent_id, path, icon, sort, component, status) VALUES
-- 一级目录
(1001, '系统管理',    'system',         1, NULL, '/system',     'Setting',        1, 'Layout',                                              1),
(1002, '质量管理',    'quality',         1, NULL, '/quality',     'Monitor',        2, 'Layout',                                              1),
(1003, '数据报表',    'report',          1, NULL, '/report',      'DataAnalysis',   3, 'Layout',                                              1),
(1004, '生产管理',    'production',      1, NULL, '/production', 'Box',           4, 'Layout',                                              1),
-- 二级菜单 - 系统管理
(1101, '部门管理',    'department:view', 2, 1001, '/system/department',   'OfficeBuilding', 1, 'system/department/index',        1),
(1102, '用户管理',    'user:view',        2, 1001, '/system/user',           'User',           2, 'system/user/index',               1),
(1103, '角色管理',    'role:view',        2, 1001, '/system/role',           'UserFilled',     3, 'system/role/index',               1),
(1104, '权限管理',    'permission:view',  2, 1001, '/system/permission',    'Lock',           4, 'system/permission/index',         1),
(1105, '操作日志',    'log:view',         2, 1001, '/system/log',           'Document',       5, 'system/log/index',                 1),
-- 二级菜单 - 质量管理
(1201, '节点管理',    'node:view',        2, 1002, '/quality/node',         'Connection',    1, 'quality/node/index',              1),
(1202, '检测项管理',  'inspection:view',  2, 1002, '/quality/inspection',   'Checked',        2, 'quality/inspection/index',         1),
(1203, '实施方案',    'plan:view',        2, 1002, '/quality/plan',         'List',           3, 'quality/plan/index',              1),
(1204, '人工录入',    'record:manual',   2, 1002, '/quality/record',       'Edit',           4, 'quality/record/index',             1),
(1205, '推送管理',    'push:view',        2, 1002, '/quality/push',         'Bell',           5, 'quality/push/index',              1),
-- 二级菜单 - 数据报表
(1301, '质量报表',    'report:view',      2, 1003, '/report/quality',       'TrendCharts',   1, 'report/quality/index',             1),
(1302, '统计概览',    'statistics:view', 2, 1003, '/report/statistics',    'PieChart',      2, 'report/statistics/index',          1),
-- 二级菜单 - 生产管理
(1401, '实施反馈',    'feedback:view',   2, 1004, '/production/feedback',  'ChatDotRound', 1, 'production/feedback/index',        1),
-- 三级按钮 - 部门管理
(1111, '部门新增',    'department:add',  3, 1101, NULL, NULL, 1, NULL, 1),
(1112, '部门编辑',    'department:edit', 3, 1101, NULL, NULL, 2, NULL, 1),
(1113, '部门删除',    'department:delete',3, 1101, NULL, NULL, 3, NULL, 1),
-- 三级按钮 - 用户管理
(1121, '用户新增',    'user:add',         3, 1102, NULL, NULL, 1, NULL, 1),
(1122, '用户编辑',    'user:edit',        3, 1102, NULL, NULL, 2, NULL, 1),
(1123, '用户删除',    'user:delete',      3, 1102, NULL, NULL, 3, NULL, 1),
(1124, '重置密码',    'user:resetPwd',   3, 1102, NULL, NULL, 4, NULL, 1),
-- 三级按钮 - 角色管理
(1131, '角色新增',    'role:add',         3, 1103, NULL, NULL, 1, NULL, 1),
(1132, '角色编辑',    'role:edit',        3, 1103, NULL, NULL, 2, NULL, 1),
(1133, '角色删除',    'role:delete',      3, 1103, NULL, NULL, 3, NULL, 1),
(1134, '权限分配',    'role:assignPerm', 3, 1103, NULL, NULL, 4, NULL, 1),
-- 三级按钮 - 权限管理
(1141, '权限新增',    'permission:add',   3, 1104, NULL, NULL, 1, NULL, 1),
(1142, '权限编辑',    'permission:edit',  3, 1104, NULL, NULL, 2, NULL, 1),
(1143, '权限删除',    'permission:delete',3, 1104, NULL, NULL, 3, NULL, 1),
-- 三级按钮 - 节点管理
(1211, '节点新增',    'node:add',         3, 1201, NULL, NULL, 1, NULL, 1),
(1212, '节点编辑',    'node:edit',        3, 1201, NULL, NULL, 2, NULL, 1),
(1213, '节点删除',    'node:delete',      3, 1201, NULL, NULL, 3, NULL, 1),
-- 三级按钮 - 检测项管理
(1221, '检测项新增',  'inspection:add',   3, 1202, NULL, NULL, 1, NULL, 1),
(1222, '检测项编辑',  'inspection:edit',  3, 1202, NULL, NULL, 2, NULL, 1),
(1223, '检测项删除',  'inspection:delete',3, 1202, NULL, NULL, 3, NULL, 1),
-- 三级按钮 - 实施方案
(1231, '方案新增',    'plan:add',         3, 1203, NULL, NULL, 1, NULL, 1),
(1232, '方案编辑',    'plan:edit',        3, 1203, NULL, NULL, 2, NULL, 1),
(1233, '方案删除',    'plan:delete',      3, 1203, NULL, NULL, 3, NULL, 1),
-- 三级按钮 - 人工录入
(1241, '数据录入',    'record:add',        3, 1204, NULL, NULL, 1, NULL, 1),
(1242, '数据修改',    'record:modify',    3, 1204, NULL, NULL, 2, NULL, 1),
-- 三级按钮 - 推送管理
(1251, '推送配置',    'push:config',      3, 1205, NULL, NULL, 1, NULL, 1),
(1252, '推送测试',    'push:test',        3, 1205, NULL, NULL, 2, NULL, 1),
-- 三级按钮 - 质量报表
(1311, '报表导出',    'report:export',    3, 1301, NULL, NULL, 1, NULL, 1),
-- 三级按钮 - 实施反馈
(1411, '反馈提交',    'feedback:add',     3, 1401, NULL, NULL, 1, NULL, 1),
(1412, '反馈编辑',    'feedback:edit',    3, 1401, NULL, NULL, 2, NULL, 1)
ON DUPLICATE KEY UPDATE name=name;

-- 分配管理员所有权限
INSERT IGNORE INTO sys_role_permission (role_id, permission_id)
SELECT 313466522431393793, id FROM sys_permission;

-- 默认管理员账号（密码：admin123，BCrypt 加密后）
-- 请在系统部署后通过注册接口或数据库手动设置
-- INSERT INTO sys_user (username, password, real_name, email, phone, status)
-- VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '管理员', 'admin@qims.com', '13800138000', 1);
