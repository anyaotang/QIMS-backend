/*
 Navicat Premium Dump SQL

 Source Server         : 192.168.0.172
 Source Server Type    : MySQL
 Source Server Version : 80042 (8.0.42)
 Source Host           : 192.168.0.172:3306
 Source Schema         : qims

 Target Server Type    : MySQL
 Target Server Version : 80042 (8.0.42)
 File Encoding         : 65001

 Date: 15/05/2026 21:34:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for qims_implementation_feedback
-- ----------------------------
DROP TABLE IF EXISTS `qims_implementation_feedback`;
CREATE TABLE `qims_implementation_feedback`  (
  `id` bigint NOT NULL COMMENT '实施反馈ID',
  `plan_id` bigint NOT NULL COMMENT '方案ID',
  `feedback_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '反馈人',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '反馈内容',
  `deleted` int NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_plan`(`plan_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '实施方案反馈表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qims_implementation_feedback
-- ----------------------------
INSERT INTO `qims_implementation_feedback` VALUES (313466522431393793, 313466522431393795, 'li_na', '2026-05-10 外径尺寸超标，按照预案调整了焊接参数，复检合格。', 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_implementation_feedback` VALUES (313466522431393794, 313466522431393796, 'chen_gang', '三坐标测量机已完成本月校准，偏差在允许范围内。', 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_implementation_feedback` VALUES (313466522431393795, 313466522431393797, 'chen_gang', '校准记录已上传至附件。', 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');

-- ----------------------------
-- Table structure for qims_implementation_plan
-- ----------------------------
DROP TABLE IF EXISTS `qims_implementation_plan`;
CREATE TABLE `qims_implementation_plan`  (
  `id` bigint NOT NULL COMMENT '实施方案ID',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '方案名称',
  `parent_id` bigint NOT NULL DEFAULT 0 COMMENT '父方案ID，0 为根',
  `responsible` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '责任人',
  `deadline` date NULL DEFAULT NULL COMMENT '截止日期',
  `status` int NOT NULL DEFAULT 0 COMMENT '0-未开始 1-进行中 2-已完成',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '描述',
  `deleted` int NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_parent`(`parent_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '实施方案表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qims_implementation_plan
-- ----------------------------
INSERT INTO `qims_implementation_plan` VALUES (313466522431393793, '年度质量计划', 0, NULL, '2026-05-15', 0, '本年度质量目标及关键措施', 0, '2026-05-15 07:46:23', '2026-05-15 16:46:27');
INSERT INTO `qims_implementation_plan` VALUES (313466522431393794, '一季度预防性维护', 313466522431393793, NULL, NULL, 0, '设备点检表、润滑计划', 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_implementation_plan` VALUES (313466522431393795, '尺寸超差处理预案', 0, NULL, NULL, 0, '当外径尺寸超出±0.2mm时的处理流程', 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_implementation_plan` VALUES (313466522431393796, '三坐标校准规程', 0, NULL, NULL, 0, '每月1日校准三坐标测量机', 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_implementation_plan` VALUES (313466522431393797, '校准记录模板', 313466522431393796, NULL, NULL, 0, '包含标准值、偏差、调整量', 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');

-- ----------------------------
-- Table structure for qims_inspection_default_value
-- ----------------------------
DROP TABLE IF EXISTS `qims_inspection_default_value`;
CREATE TABLE `qims_inspection_default_value`  (
  `id` bigint NOT NULL COMMENT '检测项默认值ID',
  `item_id` bigint NOT NULL COMMENT '检测项ID',
  `group_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分组名称',
  `default_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '默认值',
  `deleted` int NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_item`(`item_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '检测项默认值表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qims_inspection_default_value
-- ----------------------------
INSERT INTO `qims_inspection_default_value` VALUES (313466522431393793, 313466522431393796, '标准试件组', '520.0000', 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_inspection_default_value` VALUES (313466522431393794, 313466522431393797, '标准试件组', '46.0000', 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_inspection_default_value` VALUES (313466522431393795, 313466522431393798, '镜面要求组', '0.8000', 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_inspection_default_value` VALUES (313466522431393796, 313466522431393799, '低碳钢组', '0.1800', 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');

-- ----------------------------
-- Table structure for qims_inspection_item
-- ----------------------------
DROP TABLE IF EXISTS `qims_inspection_item`;
CREATE TABLE `qims_inspection_item`  (
  `id` bigint NOT NULL COMMENT '检测项ID',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '检测项名称',
  `node_id` bigint NULL DEFAULT NULL COMMENT '关联节点ID',
  `data_source` int NOT NULL DEFAULT 0 COMMENT '数据来源：0-手动 1-API 2-公式',
  `target_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '目标值',
  `unit` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '单位',
  `is_active` int NOT NULL DEFAULT 1 COMMENT '1-启用 0-禁用',
  `cron_expression` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'Cron表达式（定时采集）',
  `api_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'API地址（dataSource=1）',
  `formula` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '公式表达式（dataSource=2）',
  `push_condition` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '推送条件（Aviator表达式）',
  `push_emails` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '推送邮箱，逗号分隔',
  `deleted` int NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_node`(`node_id` ASC) USING BTREE,
  INDEX `idx_active`(`is_active` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '检测项表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qims_inspection_item
-- ----------------------------
INSERT INTO `qims_inspection_item` VALUES (313466522431393793, '外径尺寸', NULL, 0, '10.0000', 'mm', 1, '0 0 */1 * * ?', NULL, NULL, NULL, NULL, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_inspection_item` VALUES (313466522431393794, '内径尺寸', NULL, 0, '8.5000', 'mm', 1, '0 0 */1 * * ?', NULL, NULL, NULL, NULL, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_inspection_item` VALUES (313466522431393795, '壁厚', NULL, 1, '0.7500', 'mm', 1, '0 0 1 * * ?', NULL, '({DIM_001} - {DIM_002}) / 2', NULL, NULL, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_inspection_item` VALUES (313466522431393796, '抗拉强度', NULL, 2, '500.0000', 'MPa', 1, '0 0 0 ? * MON', NULL, NULL, NULL, NULL, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_inspection_item` VALUES (313466522431393797, '硬度HRC', NULL, 2, '45.0000', 'HRC', 1, '0 0 */2 * * ?', NULL, NULL, NULL, NULL, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_inspection_item` VALUES (313466522431393798, '表面光洁度', NULL, 2, '1.6000', 'Ra', 1, '0 0 10 * * ?', NULL, NULL, NULL, NULL, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_inspection_item` VALUES (313466522431393799, '碳含量', NULL, 2, '0.2200', '%', 1, '0 0 8 * * ?', NULL, NULL, NULL, NULL, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');

-- ----------------------------
-- Table structure for qims_inspection_record
-- ----------------------------
DROP TABLE IF EXISTS `qims_inspection_record`;
CREATE TABLE `qims_inspection_record`  (
  `id` bigint NOT NULL COMMENT '检测记录ID',
  `item_id` bigint NOT NULL COMMENT '检测项ID',
  `value` decimal(18, 4) NULL DEFAULT NULL COMMENT '检测值',
  `is_qualified` int NOT NULL DEFAULT 1 COMMENT '1-达标 0-不达标',
  `inspect_time` datetime NOT NULL COMMENT '检测时间',
  `data_source` int NOT NULL DEFAULT 0 COMMENT '来源：0-手动 1-API 2-公式 3-定时',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `deleted` int NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_item`(`item_id` ASC) USING BTREE,
  INDEX `idx_time`(`inspect_time` ASC) USING BTREE,
  INDEX `idx_qualified`(`is_qualified` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '检测记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qims_inspection_record
-- ----------------------------
INSERT INTO `qims_inspection_record` VALUES (313466522431393794, 313466522431393793, 10.0500, 1, '2026-05-08 12:33:21', 0, NULL, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_inspection_record` VALUES (313466522431393795, 313466522431393793, 9.9200, 1, '2026-05-09 12:33:21', 0, NULL, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_inspection_record` VALUES (313466522431393796, 313466522431393793, 10.1500, 1, '2026-05-10 12:33:21', 0, NULL, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_inspection_record` VALUES (313466522431393797, 313466522431393793, 10.0200, 1, '2026-05-11 12:33:21', 0, NULL, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_inspection_record` VALUES (313466522431393798, 313466522431393793, 9.8500, 0, '2026-05-12 12:33:21', 0, NULL, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_inspection_record` VALUES (313466522431393799, 313466522431393793, 10.0800, 1, '2026-05-13 12:33:21', 0, NULL, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_inspection_record` VALUES (313466522431393800, 313466522431393793, 10.0300, 1, '2026-05-14 12:33:21', 0, NULL, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_inspection_record` VALUES (313466522431406108, 313466522431393796, 512.0000, 1, '2026-05-10 14:30:00', 2, NULL, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_inspection_record` VALUES (313466522431406109, 313466522431393796, 488.0000, 1, '2026-05-11 09:15:00', 2, NULL, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_inspection_record` VALUES (313466522431406110, 313466522431393796, 505.0000, 1, '2026-05-12 11:00:00', 2, NULL, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_inspection_record` VALUES (313466522431406111, 313466522431393796, 521.0000, 1, '2026-05-13 08:20:00', 2, NULL, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_inspection_record` VALUES (313466522431422528, 313466522431393797, 44.8000, 1, '2026-05-09 12:33:21', 0, NULL, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_inspection_record` VALUES (313466522431422529, 313466522431393797, 45.2000, 1, '2026-05-10 12:33:21', 0, NULL, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_inspection_record` VALUES (313466522431422530, 313466522431393797, 46.1000, 1, '2026-05-11 12:33:21', 0, NULL, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_inspection_record` VALUES (313466522431422531, 313466522431393797, 45.5000, 1, '2026-05-12 12:33:21', 0, NULL, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_inspection_record` VALUES (313466522431422532, 313466522431393797, 44.9000, 1, '2026-05-13 12:33:21', 0, NULL, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_inspection_record` VALUES (313466522431422533, 313466522431393797, 45.3000, 1, '2026-05-14 12:33:21', 0, NULL, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_inspection_record` VALUES (313466522431422534, 313466522431393795, 0.7750, 1, '2026-05-08 12:33:21', 1, NULL, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_inspection_record` VALUES (313466522431422535, 313466522431393795, 0.7100, 1, '2026-05-09 12:33:21', 1, NULL, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_inspection_record` VALUES (313466522431422536, 313466522431393795, 0.8250, 1, '2026-05-10 12:33:21', 1, NULL, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_inspection_record` VALUES (313466522431422537, 313466522431393795, 0.7600, 1, '2026-05-11 12:33:21', 1, NULL, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_inspection_record` VALUES (313466522431422538, 313466522431393795, 0.6750, 1, '2026-05-12 12:33:21', 1, NULL, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_inspection_record` VALUES (313466522431422539, 313466522431393795, 0.7900, 1, '2026-05-13 12:33:21', 1, NULL, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_inspection_record` VALUES (313466522431422540, 313466522431393795, 0.7650, 1, '2026-05-14 12:33:21', 1, NULL, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');

-- ----------------------------
-- Table structure for qims_node
-- ----------------------------
DROP TABLE IF EXISTS `qims_node`;
CREATE TABLE `qims_node`  (
  `id` bigint NOT NULL COMMENT '节点ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '节点名称',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '节点类型',
  `parent_id` bigint NOT NULL DEFAULT 0 COMMENT '父节点ID，0 为根',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
  `status` int NOT NULL DEFAULT 1 COMMENT '1-启用 0-禁用',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '节点描述',
  `deleted` int NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_parent`(`parent_id` ASC) USING BTREE,
  INDEX `idx_type`(`type` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '检测节点表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qims_node
-- ----------------------------
INSERT INTO `qims_node` VALUES (313466522431393793, 'QIMS工厂', '工厂', 0, 0, 1, '/1/', 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_node` VALUES (313466522431393794, '总装车间', '车间', 313466522431393793, 1, 1, '/1/2/', 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_node` VALUES (313466522431393795, '机加工车间', '车间', 313466522431393793, 2, 1, '/1/3/', 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_node` VALUES (313466522431393796, '装配线A', '生产线', 313466522431393794, 1, 1, '/1/2/4/', 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_node` VALUES (313466522431393797, '装配线B', '生产线', 313466522431393794, 2, 1, '/1/2/5/', 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_node` VALUES (313466522431393798, '数控加工线', '生产线', 313466522431393795, 1, 1, '/1/3/6/', 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_node` VALUES (313466522431393799, '机器人焊接站', '设备', 313466522431393796, 1, 1, '/1/2/4/7/', 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_node` VALUES (313466522431393800, '尺寸检测工位', '检测工位', 313466522431393796, 2, 1, '/1/2/4/8/', 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `qims_node` VALUES (313466522431406108, '电气性能检测仪', '检测工位', 313466522431393795, 1, 1, '/1/2/5/9/', 0, '2026-05-15 07:46:23', '2026-05-15 16:45:21');
INSERT INTO `qims_node` VALUES (313466522431406109, '三坐标测量机', '设备', 313466522431393798, 1, 1, '/1/3/6/10/', 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `BLOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `SCHED_NAME`(`SCHED_NAME` ASC, `TRIGGER_NAME` ASC, `TRIGGER_GROUP` ASC) USING BTREE,
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_blob_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `CALENDAR_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_calendars
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `CRON_EXPRESSION` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `TIME_ZONE_ID` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_cron_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `ENTRY_ID` varchar(95) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `INSTANCE_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `FIRED_TIME` bigint NOT NULL,
  `SCHED_TIME` bigint NOT NULL,
  `PRIORITY` int NOT NULL,
  `STATE` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `JOB_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `JOB_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `ENTRY_ID`) USING BTREE,
  INDEX `IDX_QRTZ_FT_TRIG_INST_NAME`(`SCHED_NAME` ASC, `INSTANCE_NAME` ASC) USING BTREE,
  INDEX `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY`(`SCHED_NAME` ASC, `INSTANCE_NAME` ASC, `REQUESTS_RECOVERY` ASC) USING BTREE,
  INDEX `IDX_QRTZ_FT_J_G`(`SCHED_NAME` ASC, `JOB_NAME` ASC, `JOB_GROUP` ASC) USING BTREE,
  INDEX `IDX_QRTZ_FT_JG`(`SCHED_NAME` ASC, `JOB_GROUP` ASC) USING BTREE,
  INDEX `IDX_QRTZ_FT_T_G`(`SCHED_NAME` ASC, `TRIGGER_NAME` ASC, `TRIGGER_GROUP` ASC) USING BTREE,
  INDEX `IDX_QRTZ_FT_TG`(`SCHED_NAME` ASC, `TRIGGER_GROUP` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_fired_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `JOB_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `JOB_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `DESCRIPTION` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `IS_DURABLE` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `IS_UPDATE_DATA` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `JOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_J_REQ_RECOVERY`(`SCHED_NAME` ASC, `REQUESTS_RECOVERY` ASC) USING BTREE,
  INDEX `IDX_QRTZ_J_GRP`(`SCHED_NAME` ASC, `JOB_GROUP` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_job_details
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `LOCK_NAME` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `LOCK_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_locks
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_paused_trigger_grps
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `INSTANCE_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `LAST_CHECKIN_TIME` bigint NOT NULL,
  `CHECKIN_INTERVAL` bigint NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_scheduler_state
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `REPEAT_COUNT` bigint NOT NULL,
  `REPEAT_INTERVAL` bigint NOT NULL,
  `TIMES_TRIGGERED` bigint NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_simple_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `STR_PROP_1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `STR_PROP_2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `STR_PROP_3` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `INT_PROP_1` int NULL DEFAULT NULL,
  `INT_PROP_2` int NULL DEFAULT NULL,
  `LONG_PROP_1` bigint NULL DEFAULT NULL,
  `LONG_PROP_2` bigint NULL DEFAULT NULL,
  `DEC_PROP_1` decimal(13, 4) NULL DEFAULT NULL,
  `DEC_PROP_2` decimal(13, 4) NULL DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_simprop_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `JOB_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `JOB_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `DESCRIPTION` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint NULL DEFAULT NULL,
  `PREV_FIRE_TIME` bigint NULL DEFAULT NULL,
  `PRIORITY` int NULL DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `TRIGGER_TYPE` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `START_TIME` bigint NOT NULL,
  `END_TIME` bigint NULL DEFAULT NULL,
  `CALENDAR_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `MISFIRE_INSTR` smallint NULL DEFAULT NULL,
  `JOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_J`(`SCHED_NAME` ASC, `JOB_NAME` ASC, `JOB_GROUP` ASC) USING BTREE,
  INDEX `IDX_QRTZ_T_JG`(`SCHED_NAME` ASC, `JOB_GROUP` ASC) USING BTREE,
  INDEX `IDX_QRTZ_T_C`(`SCHED_NAME` ASC, `CALENDAR_NAME` ASC) USING BTREE,
  INDEX `IDX_QRTZ_T_G`(`SCHED_NAME` ASC, `TRIGGER_GROUP` ASC) USING BTREE,
  INDEX `IDX_QRTZ_T_STATE`(`SCHED_NAME` ASC, `TRIGGER_STATE` ASC) USING BTREE,
  INDEX `IDX_QRTZ_T_N_STATE`(`SCHED_NAME` ASC, `TRIGGER_NAME` ASC, `TRIGGER_GROUP` ASC, `TRIGGER_STATE` ASC) USING BTREE,
  INDEX `IDX_QRTZ_T_N_G_STATE`(`SCHED_NAME` ASC, `TRIGGER_GROUP` ASC, `TRIGGER_STATE` ASC) USING BTREE,
  INDEX `IDX_QRTZ_T_NEXT_FIRE_TIME`(`SCHED_NAME` ASC, `NEXT_FIRE_TIME` ASC) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_ST`(`SCHED_NAME` ASC, `TRIGGER_STATE` ASC, `NEXT_FIRE_TIME` ASC) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_MISFIRE`(`SCHED_NAME` ASC, `MISFIRE_INSTR` ASC, `NEXT_FIRE_TIME` ASC) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_ST_MISFIRE`(`SCHED_NAME` ASC, `MISFIRE_INSTR` ASC, `NEXT_FIRE_TIME` ASC, `TRIGGER_STATE` ASC) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP`(`SCHED_NAME` ASC, `MISFIRE_INSTR` ASC, `NEXT_FIRE_TIME` ASC, `TRIGGER_GROUP` ASC, `TRIGGER_STATE` ASC) USING BTREE,
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `qrtz_job_details` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for sys_department
-- ----------------------------
DROP TABLE IF EXISTS `sys_department`;
CREATE TABLE `sys_department`  (
  `id` bigint NOT NULL COMMENT '部门ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '部门名称',
  `parent_id` bigint NOT NULL DEFAULT 0 COMMENT '父部门ID，0 为根',
  `inherit_role_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '继承角色ID，逗号分隔',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
  `status` int NOT NULL DEFAULT 1 COMMENT '1-启用 0-禁用',
  `deleted` int NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_parent`(`parent_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_department
-- ----------------------------
INSERT INTO `sys_department` VALUES (313466522431393793, 'QIMS总公司', 0, '', 0, 1, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `sys_department` VALUES (313466522431393794, '生产部', 313466522431393793, '4', 1, 1, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `sys_department` VALUES (313466522431393795, '一车间', 313466522431393794, '', 2, 1, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `sys_department` VALUES (313466522431393796, '二车间', 313466522431393794, '', 2, 1, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `sys_department` VALUES (313466522431393797, '质量部', 313466522431393793, '2', 1, 1, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `sys_department` VALUES (313466522431393798, 'IQC来料检验', 313466522431393797, '', 2, 1, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `sys_department` VALUES (313466522431393799, 'PQC过程检验', 313466522431393797, '', 2, 1, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `sys_department` VALUES (313466522431393800, 'OQC成品检验', 313466522431393797, '', 2, 1, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `sys_department` VALUES (2055139246517424129, '三车间', 313466522431393794, NULL, 0, 1, 0, '2026-05-15 12:12:34', '2026-05-15 19:44:34');
INSERT INTO `sys_department` VALUES (2055252964920193026, 'cws', 313466522431393794, NULL, 0, 1, 0, '2026-05-15 19:44:26', '2026-05-15 19:44:26');

-- ----------------------------
-- Table structure for sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log`  (
  `id` bigint NOT NULL COMMENT '日志ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作用户',
  `module` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作模块',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作描述',
  `method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求方法',
  `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求URL',
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求IP',
  `params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '请求参数',
  `result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '返回结果',
  `duration` bigint NULL DEFAULT NULL COMMENT '耗时(ms)',
  `deleted` int NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_username`(`username` ASC) USING BTREE,
  INDEX `idx_module`(`module` ASC) USING BTREE,
  INDEX `idx_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_operation_log
-- ----------------------------
INSERT INTO `sys_operation_log` VALUES (313466522431442945, 'admin', '新增检测项', NULL, 'POST', NULL, '192.168.1.100', '{\"code\":\"DIM_003\"}', NULL, 45, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `sys_operation_log` VALUES (313466522431442946, 'zhang_wei', '导出质量报表', NULL, 'GET', NULL, '192.168.1.102', '{\"dateRange\":\"month\"}', NULL, 120, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `sys_operation_log` VALUES (313466522431442947, 'li_na', '人工修改数据', NULL, 'PUT', NULL, '192.168.1.103', '{\"recordId\":5,\"newValue\":9.85}', NULL, 32, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `id` bigint NOT NULL COMMENT '权限ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限编码，如 node:edit',
  `type` int NOT NULL DEFAULT 1 COMMENT '类型(1菜单2按钮)',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父级ID',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `status` int NOT NULL DEFAULT 1 COMMENT '1-启用 0-禁用',
  `deleted` int NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '前端路由路径',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序（同级内序号）',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '前端组件路径',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `code`(`code` ASC) USING BTREE,
  INDEX `idx_code`(`code` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES (1001, '系统管理', 'system', 1, NULL, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', '/system', 'Setting', 1, 'Layout');
INSERT INTO `sys_permission` VALUES (1002, '质量管理', 'quality', 1, NULL, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', '/quality', 'Monitor', 2, 'Layout');
INSERT INTO `sys_permission` VALUES (1003, '数据报表', 'report', 1, NULL, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', '/report', 'DataAnalysis', 3, 'Layout');
INSERT INTO `sys_permission` VALUES (1004, '生产管理', 'production', 1, NULL, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', '/production', 'Box', 4, 'Layout');
INSERT INTO `sys_permission` VALUES (1101, '部门管理', 'department:view', 2, 1001, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', '/system/department', 'OfficeBuilding', 1, 'system/department/index');
INSERT INTO `sys_permission` VALUES (1102, '用户管理', 'user:view', 2, 1001, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', '/system/user', 'User', 2, 'system/user/index');
INSERT INTO `sys_permission` VALUES (1103, '角色管理', 'role:view', 2, 1001, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', '/system/role', 'UserFilled', 3, 'system/role/index');
INSERT INTO `sys_permission` VALUES (1104, '权限管理', 'permission:view', 2, 1001, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', '/system/permission', 'Lock', 4, 'system/permission/index');
INSERT INTO `sys_permission` VALUES (1105, '操作日志', 'log:view', 2, 1001, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', '/system/log', 'Document', 5, 'system/log/index');
INSERT INTO `sys_permission` VALUES (1111, '部门新增', 'department:add', 3, 1101, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', NULL, NULL, 1, NULL);
INSERT INTO `sys_permission` VALUES (1112, '部门编辑', 'department:edit', 3, 1101, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', NULL, NULL, 2, NULL);
INSERT INTO `sys_permission` VALUES (1113, '部门删除', 'department:delete', 3, 1101, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', NULL, NULL, 3, NULL);
INSERT INTO `sys_permission` VALUES (1121, '用户新增', 'user:add', 3, 1102, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', NULL, NULL, 1, NULL);
INSERT INTO `sys_permission` VALUES (1122, '用户编辑', 'user:edit', 3, 1102, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', NULL, NULL, 2, NULL);
INSERT INTO `sys_permission` VALUES (1123, '用户删除', 'user:delete', 3, 1102, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', NULL, NULL, 3, NULL);
INSERT INTO `sys_permission` VALUES (1124, '重置密码', 'user:resetPwd', 3, 1102, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', NULL, NULL, 4, NULL);
INSERT INTO `sys_permission` VALUES (1131, '角色新增', 'role:add', 3, 1103, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', NULL, NULL, 1, NULL);
INSERT INTO `sys_permission` VALUES (1132, '角色编辑', 'role:edit', 3, 1103, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', NULL, NULL, 2, NULL);
INSERT INTO `sys_permission` VALUES (1133, '角色删除', 'role:delete', 3, 1103, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', NULL, NULL, 3, NULL);
INSERT INTO `sys_permission` VALUES (1134, '权限分配', 'role:assignPerm', 3, 1103, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', NULL, NULL, 4, NULL);
INSERT INTO `sys_permission` VALUES (1141, '权限新增', 'permission:add', 3, 1104, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', NULL, NULL, 1, NULL);
INSERT INTO `sys_permission` VALUES (1142, '权限编辑', 'permission:edit', 3, 1104, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', NULL, NULL, 2, NULL);
INSERT INTO `sys_permission` VALUES (1143, '权限删除', 'permission:delete', 3, 1104, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', NULL, NULL, 3, NULL);
INSERT INTO `sys_permission` VALUES (1201, '节点管理', 'node:view', 2, 1002, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', '/quality/node', 'Connection', 1, 'quality/node/index');
INSERT INTO `sys_permission` VALUES (1202, '检测项管理', 'inspection:view', 2, 1002, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', '/quality/inspection', 'Checked', 2, 'quality/inspection/index');
INSERT INTO `sys_permission` VALUES (1203, '实施方案', 'plan:view', 2, 1002, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', '/quality/plan', 'List', 3, 'quality/plan/index');
INSERT INTO `sys_permission` VALUES (1204, '人工录入', 'record:manual', 2, 1002, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', '/quality/record', 'Edit', 4, 'quality/record/index');
INSERT INTO `sys_permission` VALUES (1205, '推送管理', 'push:view', 2, 1002, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', '/quality/push', 'Bell', 5, 'quality/push/index');
INSERT INTO `sys_permission` VALUES (1211, '节点新增', 'node:add', 3, 1201, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', NULL, NULL, 1, NULL);
INSERT INTO `sys_permission` VALUES (1212, '节点编辑', 'node:edit', 3, 1201, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', NULL, NULL, 2, NULL);
INSERT INTO `sys_permission` VALUES (1213, '节点删除', 'node:delete', 3, 1201, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', NULL, NULL, 3, NULL);
INSERT INTO `sys_permission` VALUES (1221, '检测项新增', 'inspection:add', 3, 1202, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', NULL, NULL, 1, NULL);
INSERT INTO `sys_permission` VALUES (1222, '检测项编辑', 'inspection:edit', 3, 1202, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', NULL, NULL, 2, NULL);
INSERT INTO `sys_permission` VALUES (1223, '检测项删除', 'inspection:delete', 3, 1202, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', NULL, NULL, 3, NULL);
INSERT INTO `sys_permission` VALUES (1231, '方案新增', 'plan:add', 3, 1203, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', NULL, NULL, 1, NULL);
INSERT INTO `sys_permission` VALUES (1232, '方案编辑', 'plan:edit', 3, 1203, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', NULL, NULL, 2, NULL);
INSERT INTO `sys_permission` VALUES (1233, '方案删除', 'plan:delete', 3, 1203, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', NULL, NULL, 3, NULL);
INSERT INTO `sys_permission` VALUES (1241, '数据录入', 'record:add', 3, 1204, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', NULL, NULL, 1, NULL);
INSERT INTO `sys_permission` VALUES (1242, '数据修改', 'record:modify', 3, 1204, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', NULL, NULL, 2, NULL);
INSERT INTO `sys_permission` VALUES (1251, '推送配置', 'push:config', 3, 1205, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', NULL, NULL, 1, NULL);
INSERT INTO `sys_permission` VALUES (1252, '推送测试', 'push:test', 3, 1205, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', NULL, NULL, 2, NULL);
INSERT INTO `sys_permission` VALUES (1301, '质量报表', 'report:view', 2, 1003, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', '/report/quality', 'TrendCharts', 1, 'report/quality/index');
INSERT INTO `sys_permission` VALUES (1302, '统计概览', 'statistics:view', 2, 1003, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', '/report/statistics', 'PieChart', 2, 'report/statistics/index');
INSERT INTO `sys_permission` VALUES (1311, '报表导出', 'report:export', 3, 1301, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', NULL, NULL, 1, NULL);
INSERT INTO `sys_permission` VALUES (1401, '实施反馈', 'feedback:view', 2, 1004, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', '/production/feedback', 'ChatDotRound', 1, 'production/feedback/index');
INSERT INTO `sys_permission` VALUES (1411, '反馈提交', 'feedback:add', 3, 1401, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', NULL, NULL, 1, NULL);
INSERT INTO `sys_permission` VALUES (1412, '反馈编辑', 'feedback:edit', 3, 1401, NULL, 1, 0, '2026-05-15 10:49:48', '2026-05-15 10:49:48', NULL, NULL, 2, NULL);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint NOT NULL COMMENT '角色ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色编码',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `status` int NOT NULL DEFAULT 1 COMMENT '1-启用 0-禁用',
  `data_scope` int NOT NULL DEFAULT 1 COMMENT '数据范围(1全部2本部门3本部门及子部门4仅本人)',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `deleted` int NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `code`(`code` ASC) USING BTREE,
  INDEX `idx_code`(`code` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (313466522431393793, '系统管理员', 'ROLE_ADMIN', NULL, 1, 1, NULL, 0, '2026-05-15 07:46:23', '2026-05-15 08:04:03');
INSERT INTO `sys_role` VALUES (313466522431393794, '质量经理', 'ROLE_QUALITY_MANAGER', NULL, 1, 2, NULL, 0, '2026-05-15 07:46:23', '2026-05-15 11:36:43');
INSERT INTO `sys_role` VALUES (313466522431393795, '质检员', 'ROLE_INSPECTOR', NULL, 1, 4, NULL, 0, '2026-05-15 07:46:23', '2026-05-15 11:36:43');
INSERT INTO `sys_role` VALUES (313466522431393796, '生产主管', 'ROLE_PRODUCTION_MANAGER', NULL, 1, 2, NULL, 0, '2026-05-15 07:46:23', '2026-05-15 11:36:43');
INSERT INTO `sys_role` VALUES (313466522431393797, '一线员工', 'ROLE_WORKER', NULL, 1, 4, NULL, 0, '2026-05-15 07:46:23', '2026-05-15 11:36:43');

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
  `id` bigint NOT NULL COMMENT '角色权限关联ID',
  `role_id` bigint NOT NULL,
  `permission_id` bigint NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_role_perm`(`role_id` ASC, `permission_id` ASC) USING BTREE,
  INDEX `idx_role`(`role_id` ASC) USING BTREE,
  INDEX `idx_perm`(`permission_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 51 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色权限关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES (5000000001, 313466522431393797, 1003, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (5000000002, 313466522431393797, 1301, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (6000000001, 313466522431393796, 1003, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (6000000002, 313466522431393796, 1301, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (6000000003, 313466522431393796, 1302, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (6000000010, 313466522431393796, 1004, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (6000000011, 313466522431393796, 1401, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (6000000012, 313466522431393796, 1411, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (6000000013, 313466522431393796, 1412, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (7000000001, 313466522431393795, 1002, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (7000000002, 313466522431393795, 1202, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (7000000003, 313466522431393795, 1204, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (7000000004, 313466522431393795, 1241, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (7000000005, 313466522431393795, 1242, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (7000000010, 313466522431393795, 1003, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (7000000011, 313466522431393795, 1301, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (7000000012, 313466522431393795, 1311, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (8000000001, 313466522431393794, 1002, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (8000000002, 313466522431393794, 1201, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (8000000003, 313466522431393794, 1202, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (8000000004, 313466522431393794, 1203, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (8000000005, 313466522431393794, 1204, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (8000000006, 313466522431393794, 1205, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (8000000011, 313466522431393794, 1211, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (8000000012, 313466522431393794, 1212, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (8000000013, 313466522431393794, 1213, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (8000000014, 313466522431393794, 1221, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (8000000015, 313466522431393794, 1222, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (8000000016, 313466522431393794, 1223, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (8000000017, 313466522431393794, 1231, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (8000000018, 313466522431393794, 1232, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (8000000019, 313466522431393794, 1233, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (8000000020, 313466522431393794, 1241, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (8000000021, 313466522431393794, 1242, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (8000000022, 313466522431393794, 1251, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (8000000023, 313466522431393794, 1252, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (8000000030, 313466522431393794, 1003, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (8000000031, 313466522431393794, 1301, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (8000000032, 313466522431393794, 1302, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (8000000033, 313466522431393794, 1311, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001001, 313466522431393793, 1001, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001002, 313466522431393793, 1002, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001003, 313466522431393793, 1003, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001004, 313466522431393793, 1004, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001101, 313466522431393793, 1101, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001102, 313466522431393793, 1102, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001103, 313466522431393793, 1103, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001104, 313466522431393793, 1104, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001105, 313466522431393793, 1105, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001111, 313466522431393793, 1111, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001112, 313466522431393793, 1112, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001113, 313466522431393793, 1113, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001121, 313466522431393793, 1121, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001122, 313466522431393793, 1122, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001123, 313466522431393793, 1123, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001124, 313466522431393793, 1124, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001131, 313466522431393793, 1131, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001132, 313466522431393793, 1132, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001133, 313466522431393793, 1133, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001134, 313466522431393793, 1134, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001141, 313466522431393793, 1141, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001142, 313466522431393793, 1142, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001143, 313466522431393793, 1143, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001201, 313466522431393793, 1201, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001202, 313466522431393793, 1202, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001203, 313466522431393793, 1203, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001204, 313466522431393793, 1204, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001205, 313466522431393793, 1205, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001211, 313466522431393793, 1211, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001212, 313466522431393793, 1212, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001213, 313466522431393793, 1213, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001221, 313466522431393793, 1221, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001222, 313466522431393793, 1222, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001223, 313466522431393793, 1223, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001231, 313466522431393793, 1231, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001232, 313466522431393793, 1232, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001233, 313466522431393793, 1233, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001241, 313466522431393793, 1241, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001242, 313466522431393793, 1242, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001251, 313466522431393793, 1251, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001252, 313466522431393793, 1252, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001301, 313466522431393793, 1301, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001302, 313466522431393793, 1302, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001311, 313466522431393793, 1311, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001401, 313466522431393793, 1401, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001411, 313466522431393793, 1411, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);
INSERT INTO `sys_role_permission` VALUES (9000001412, 313466522431393793, 1412, '2026-05-15 10:49:48', '2026-05-15 10:49:48', 0);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint NOT NULL COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'BCrypt加密密码',
  `real_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `department_id` bigint NULL DEFAULT NULL COMMENT '所属部门ID',
  `status` int NOT NULL DEFAULT 1 COMMENT '1-启用 0-禁用',
  `deleted` int NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE,
  INDEX `idx_username`(`username` ASC) USING BTREE,
  INDEX `idx_department`(`department_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (313466522431393793, 'admin', '$2b$10$Ix58Yb4c6Ldxq/oyGjLvgeGT6n3CRoz6LpnPuT7zXSzMgy7xkbDpW', NULL, 'admin@qims.com', NULL, 313466522431393793, 1, 0, '2026-05-15 07:46:23', '2026-05-15 09:33:14');
INSERT INTO `sys_user` VALUES (313466522431393794, 'zhang_wei', '$2b$10$Ix58Yb4c6Ldxq/oyGjLvgeGT6n3CRoz6LpnPuT7zXSzMgy7xkbDpW', NULL, 'zhang.wei@qims.com', NULL, 313466522431393797, 1, 0, '2026-05-15 07:46:23', '2026-05-15 09:33:14');
INSERT INTO `sys_user` VALUES (313466522431393795, 'li_na', '$2b$10$Ix58Yb4c6Ldxq/oyGjLvgeGT6n3CRoz6LpnPuT7zXSzMgy7xkbDpW', NULL, 'li.na@qims.com', NULL, 313466522431393798, 1, 0, '2026-05-15 07:46:23', '2026-05-15 09:33:14');
INSERT INTO `sys_user` VALUES (313466522431393796, 'wang_fang', '$2b$10$Ix58Yb4c6Ldxq/oyGjLvgeGT6n3CRoz6LpnPuT7zXSzMgy7xkbDpW', NULL, 'wang.fang@qims.com', NULL, 313466522431393799, 1, 0, '2026-05-15 07:46:23', '2026-05-15 09:33:14');
INSERT INTO `sys_user` VALUES (313466522431393797, 'chen_gang', '$2b$10$Ix58Yb4c6Ldxq/oyGjLvgeGT6n3CRoz6LpnPuT7zXSzMgy7xkbDpW', NULL, 'chen.gang@qims.com', NULL, 313466522431393795, 1, 0, '2026-05-15 07:46:23', '2026-05-15 09:33:14');
INSERT INTO `sys_user` VALUES (313466522431393798, 'li_hua', '$2a$10$RA0HUyLP/szdR3e0Tmtw9Ogrgl5e7I6aUJEjLDfsOMgKMy2muYe9m', NULL, 'li.hua@qims.com', NULL, 313466522431393796, 1, 0, '2026-05-15 07:46:23', '2026-05-15 09:33:14');
INSERT INTO `sys_user` VALUES (2055140021700300801, '测试', '', '', '', '', NULL, 1, 1, '2026-05-15 12:15:38', '2026-05-15 19:48:01');
INSERT INTO `sys_user` VALUES (2055254321857228801, 'ce1', '', '', '', '', NULL, 1, 0, '2026-05-15 19:49:50', '2026-05-15 19:49:50');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint NOT NULL COMMENT '用户角色关联ID',
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_role`(`user_id` ASC, `role_id` ASC) USING BTREE,
  INDEX `idx_user`(`user_id` ASC) USING BTREE,
  INDEX `idx_role`(`role_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (313466522431434753, 313466522431393793, 313466522431393793, '2026-05-15 07:46:23', '2026-05-15 08:04:03', 0);
INSERT INTO `sys_user_role` VALUES (313466522431434754, 313466522431393794, 313466522431393794, '2026-05-15 07:46:23', '2026-05-15 08:04:03', 0);
INSERT INTO `sys_user_role` VALUES (313466522431434755, 313466522431393795, 313466522431393795, '2026-05-15 07:46:23', '2026-05-15 08:04:03', 0);
INSERT INTO `sys_user_role` VALUES (313466522431434756, 313466522431393796, 313466522431393795, '2026-05-15 07:46:23', '2026-05-15 08:04:03', 0);
INSERT INTO `sys_user_role` VALUES (313466522431434757, 313466522431393797, 313466522431393796, '2026-05-15 07:46:23', '2026-05-15 08:04:03', 0);
INSERT INTO `sys_user_role` VALUES (313466522431434758, 313466522431393798, 313466522431393797, '2026-05-15 07:46:23', '2026-05-15 08:04:03', 0);

SET FOREIGN_KEY_CHECKS = 1;
