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
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
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
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
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
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL COMMENT '角色名称',
    code        VARCHAR(100) NOT NULL UNIQUE COMMENT '角色编码',
    remark      VARCHAR(255) NULL COMMENT '备注',
    status      INT         NOT NULL DEFAULT 1 COMMENT '1-启用 0-禁用',
    deleted     INT         NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    create_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- =====================================================
-- 4. 权限表（支持菜单树结构）
-- =====================================================
CREATE TABLE IF NOT EXISTS sys_permission (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL COMMENT '权限名称',
    code        VARCHAR(100) NOT NULL UNIQUE COMMENT '权限编码，如 node:edit',
    type        INT         NOT NULL DEFAULT 1 COMMENT '类型(1菜单2按钮)',
    parent_id   BIGINT       DEFAULT NULL COMMENT '父级ID',
    status      INT         NOT NULL DEFAULT 1 COMMENT '1-启用 0-禁用',
    deleted     INT         NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    create_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_code (code),
    INDEX idx_parent (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- =====================================================
-- 5. 用户-角色关联表
-- =====================================================
CREATE TABLE IF NOT EXISTS sys_user_role (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
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
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
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
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100)  NOT NULL COMMENT '节点名称',
    type        VARCHAR(50)   NOT NULL COMMENT '节点类型',
    parent_id   BIGINT        NOT NULL DEFAULT 0 COMMENT '父节点ID，0 为根',
    sort        INT            NOT NULL DEFAULT 0 COMMENT '排序',
    status      INT            NOT NULL DEFAULT 1 COMMENT '1-启用 0-禁用',
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
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
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
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
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
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
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
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
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
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
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
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
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
-- 14. 初始化数据：默认管理员角色和权限
-- =====================================================

-- 默认角色：管理员
INSERT INTO sys_role (name, code, remark, status)
VALUES ('管理员', 'ROLE_ADMIN', '系统管理员，拥有所有权限', 1)
ON DUPLICATE KEY UPDATE code=code;

-- 菜单数据（REPLACE INTO 兼容已有记录，自动处理 type/parent_id 更新）
REPLACE INTO sys_permission (id, name, code, type, parent_id, status) VALUES
-- 父级菜单
(100, '系统管理',   'system',       1, NULL, 1),
(101, '质量检测',   'inspection',   1, NULL, 1),
(102, '检测项目管理','item_group',  1, 101,  1),
(103, '检测记录管理','record_group',1, 101,  1),
-- 系统管理子菜单
(1,   '部门管理',   'dept:manage',  1, 100,  1),
(2,   '用户管理',   'user:manage',  1, 100,  1),
(3,   '角色管理',   'role:manage',  1, 100,  1),
(4,   '权限管理',   'perm:manage',  1, 100,  1),
-- 质量检测子菜单
(5,   '节点管理',   'node:manage',  1, 101,  1),
(6,   '节点查看',   'node:view',    1, 101,  1),
-- 检测项目管理分组
(7,   '检测项管理', 'item:manage',  1, 102,  1),
(8,   '检测项查看', 'item:view',    1, 102,  1),
-- 检测记录管理分组
(9,   '记录录入',   'record:manual',1, 103,  1),
(10,  '记录查看',   'record:view', 1, 103,  1),
-- 质量检测同级菜单
(11,  '报表查看',   'report:view', 1, 101,  1),
(12,  '报表导出',   'report:export',1, 101,  1),
(13,  '方案管理',   'plan:manage',  1, NULL,  1),
(14,  '日志查看',   'log:view',     2, NULL,  1);

-- 将全部权限分配给管理员角色
INSERT IGNORE INTO sys_role_permission (role_id, permission_id)
SELECT r.id, p.id
FROM sys_role r, sys_permission p
WHERE r.code = 'ROLE_ADMIN';

-- 默认管理员账号（密码：admin123，BCrypt 加密后）
-- 请在系统部署后通过注册接口或数据库手动设置
-- INSERT INTO sys_user (username, password, real_name, email, phone, status)
-- VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '管理员', 'admin@qims.com', '13800138000', 1);
