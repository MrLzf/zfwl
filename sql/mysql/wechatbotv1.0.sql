/*
 Navicat Premium Dump SQL

 Source Server         : 家教
 Source Server Type    : MySQL
 Source Server Version : 80407 (8.4.7)
 Source Host           : 42.194.173.153:3306
 Source Schema         : wechatbot

 Target Server Type    : MySQL
 Target Server Version : 80407 (8.4.7)
 File Encoding         : 65001

 Date: 23/05/2026 16:34:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for daily_stat
-- ----------------------------
DROP TABLE IF EXISTS `daily_stat`;
CREATE TABLE `daily_stat`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `stat_date` date NOT NULL,
  `invite_count` int NOT NULL DEFAULT 0,
  `reply_count` int NOT NULL DEFAULT 0,
  `unmatched_count` int NOT NULL DEFAULT 0,
  `exception_count` int NOT NULL DEFAULT 0,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_stat_date`(`stat_date` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of daily_stat
-- ----------------------------
INSERT INTO `daily_stat` VALUES (1, '2026-05-22', 1, 0, 0, 0, '2026-05-22 20:01:20', '2026-05-22 20:01:20', 0);
INSERT INTO `daily_stat` VALUES (2, '2026-05-23', 7, 8, 2, 2, '2026-05-23 12:41:45', '2026-05-23 12:41:45', 0);

-- ----------------------------
-- Table structure for group_keyword_bind
-- ----------------------------
DROP TABLE IF EXISTS `group_keyword_bind`;
CREATE TABLE `group_keyword_bind`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `keyword_rule_id` bigint NOT NULL,
  `group_id` bigint NOT NULL,
  `sort_order` int NOT NULL DEFAULT 0,
  `status` tinyint NOT NULL DEFAULT 1,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_keyword_bind`(`keyword_rule_id` ASC, `status` ASC, `sort_order` ASC) USING BTREE,
  INDEX `idx_group_bind`(`group_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of group_keyword_bind
-- ----------------------------
INSERT INTO `group_keyword_bind` VALUES (1, 1, 1, 0, 1, '2026-05-22 19:06:20', '2026-05-22 19:06:20', 0);
INSERT INTO `group_keyword_bind` VALUES (2, 1, 2, 1, 1, '2026-05-22 19:06:20', '2026-05-22 19:06:20', 0);

-- ----------------------------
-- Table structure for keyword_rule
-- ----------------------------
DROP TABLE IF EXISTS `keyword_rule`;
CREATE TABLE `keyword_rule`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `keyword` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `match_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'EXACT',
  `trigger_scene` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PRIVATE_CHAT',
  `rule_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `priority` int NOT NULL DEFAULT 0,
  `status` tinyint NOT NULL DEFAULT 1,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_keyword_scene`(`keyword` ASC, `trigger_scene` ASC, `status` ASC) USING BTREE,
  INDEX `idx_rule_type`(`rule_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of keyword_rule
-- ----------------------------
INSERT INTO `keyword_rule` VALUES (1, '进群', 'EXACT', 'PRIVATE_CHAT', 'GROUP_INVITE', 100, 1, '2026-05-22 19:06:20', '2026-05-22 19:06:20', 0);
INSERT INTO `keyword_rule` VALUES (2, '价格', 'EXACT', 'PRIVATE_CHAT', 'AUTO_REPLY', 90, 1, '2026-05-22 19:06:20', '2026-05-22 19:06:20', 0);
INSERT INTO `keyword_rule` VALUES (3, '资料', 'EXACT', 'PRIVATE_CHAT', 'AUTO_REPLY', 80, 1, '2026-05-22 19:06:20', '2026-05-22 19:06:20', 0);

-- ----------------------------
-- Table structure for operation_log
-- ----------------------------
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `external_user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `user_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `trigger_scene` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `operation_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `keyword` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `status` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `response_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `failure_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_operation_created`(`operation_type` ASC, `created_at` ASC) USING BTREE,
  INDEX `idx_user_created`(`external_user_id` ASC, `created_at` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of operation_log
-- ----------------------------
INSERT INTO `operation_log` VALUES (1, 'demo-user', '测试用户', 'PRIVATE_CHAT', 'GROUP_INVITE', '进群', 'SUCCESS', 'Invited to 学习交流群A', NULL, '2026-05-22 20:01:20', '2026-05-22 20:01:20', 0);
INSERT INTO `operation_log` VALUES (2, 'LiZhaoFu', 'LiZhaoFu', 'PRIVATE_CHAT', 'GROUP_INVITE', '进群', 'FAILED', NULL, 'WeCom invite failed', '2026-05-23 12:41:45', '2026-05-23 12:41:45', 0);
INSERT INTO `operation_log` VALUES (3, 'LiZhaoFu', 'LiZhaoFu', 'PRIVATE_CHAT', 'GROUP_INVITE', '进群', 'FAILED', NULL, 'WeCom invite failed', '2026-05-23 12:42:05', '2026-05-23 12:42:05', 0);
INSERT INTO `operation_log` VALUES (4, 'LiZhaoFu', 'LiZhaoFu', 'PRIVATE_CHAT', 'GROUP_INVITE', '进群', 'SUCCESS', 'Invited to 学习交流群A', NULL, '2026-05-23 12:44:03', '2026-05-23 12:44:03', 0);
INSERT INTO `operation_log` VALUES (5, 'wmIub4WwAAvFHkyK_CB_OMGcLPUkx50A', 'wmIub4WwAAvFHkyK_CB_OMGcLPUkx50A', 'PRIVATE_CHAT', 'GROUP_INVITE', '进群', 'SUCCESS', '欢迎加入学习交流群，请先阅读群公告。\n请点击以下链接或扫码进群：https://example.com/group-a-qrcode.png', NULL, '2026-05-23 13:55:15', '2026-05-23 13:55:15', 0);
INSERT INTO `operation_log` VALUES (6, 'wmIub4WwAA1Zb2vZd4WVXXSNiKKHpPQw', 'wmIub4WwAA1Zb2vZd4WVXXSNiKKHpPQw', 'PRIVATE_CHAT', 'AUTO_REPLY', '价格', 'SUCCESS', '您好，当前产品价格请以客服最新报价为准，您也可以留下需求，我们会尽快跟进。', NULL, '2026-05-23 15:26:26', '2026-05-23 15:26:26', 0);
INSERT INTO `operation_log` VALUES (7, 'wmIub4WwAA1Zb2vZd4WVXXSNiKKHpPQw', 'wmIub4WwAA1Zb2vZd4WVXXSNiKKHpPQw', 'PRIVATE_CHAT', 'AUTO_REPLY', '资料', 'SUCCESS', '资料领取链接：https://example.com/materials', NULL, '2026-05-23 15:27:02', '2026-05-23 15:27:02', 0);
INSERT INTO `operation_log` VALUES (8, 'wmIub4WwAA1Zb2vZd4WVXXSNiKKHpPQw', 'wmIub4WwAA1Zb2vZd4WVXXSNiKKHpPQw', 'PRIVATE_CHAT', 'GROUP_INVITE', '进群', 'SUCCESS', '欢迎加入学习交流群，请先阅读群公告。\n请点击以下链接或扫码进群：https://example.com/group-a-qrcode.png', NULL, '2026-05-23 15:27:13', '2026-05-23 15:27:13', 0);
INSERT INTO `operation_log` VALUES (9, 'wmIub4WwAA1Zb2vZd4WVXXSNiKKHpPQw', 'wmIub4WwAA1Zb2vZd4WVXXSNiKKHpPQw', 'PRIVATE_CHAT', 'GROUP_INVITE', '进群', 'SKIPPED', '您已在群内，无需重复进群', 'Duplicate invite blocked', '2026-05-23 15:45:26', '2026-05-23 15:45:26', 0);
INSERT INTO `operation_log` VALUES (10, 'wmIub4WwAA1Zb2vZd4WVXXSNiKKHpPQw', 'wmIub4WwAA1Zb2vZd4WVXXSNiKKHpPQw', 'PRIVATE_CHAT', 'GROUP_INVITE', '进群', 'SUCCESS', '欢迎加入备用学习交流群，请先阅读群公告。\n请点击以下链接或扫码进群：https://wccprint.top:8443/uploads/qrcodes/2026-05-23/89ff0f57-8ec4-4033-892f-302512f18c7e.png', NULL, '2026-05-23 15:45:55', '2026-05-23 15:45:55', 0);
INSERT INTO `operation_log` VALUES (11, 'wmIub4WwAA1Zb2vZd4WVXXSNiKKHpPQw', 'wmIub4WwAA1Zb2vZd4WVXXSNiKKHpPQw', 'PRIVATE_CHAT', 'GROUP_INVITE', '进群', 'SKIPPED', '您已在群内，无需重复进群', 'Duplicate invite blocked', '2026-05-23 15:53:27', '2026-05-23 15:53:27', 0);
INSERT INTO `operation_log` VALUES (12, 'wmIub4WwAA1Zb2vZd4WVXXSNiKKHpPQw', 'wmIub4WwAA1Zb2vZd4WVXXSNiKKHpPQw', 'PRIVATE_CHAT', 'GROUP_INVITE', '进群', 'SKIPPED', '您已在群内，无需重复进群', 'Duplicate invite blocked', '2026-05-23 15:53:46', '2026-05-23 15:53:46', 0);
INSERT INTO `operation_log` VALUES (13, 'wmIub4WwAA1Zb2vZd4WVXXSNiKKHpPQw', 'wmIub4WwAA1Zb2vZd4WVXXSNiKKHpPQw', 'PRIVATE_CHAT', 'GROUP_INVITE', '进群', 'SKIPPED', '您已在群内，无需重复进群', 'Duplicate invite blocked', '2026-05-23 15:53:57', '2026-05-23 15:53:57', 0);
INSERT INTO `operation_log` VALUES (14, 'wmIub4WwAA8nMLkNSv-isjQp3FEXE3ag', 'wmIub4WwAA8nMLkNSv-isjQp3FEXE3ag', 'PRIVATE_CHAT', 'FALLBACK', '你好', 'SUCCESS', '暂无相关解答，可私信客服咨询详情', NULL, '2026-05-23 15:57:58', '2026-05-23 15:57:58', 0);
INSERT INTO `operation_log` VALUES (15, 'wmIub4WwAA8nMLkNSv-isjQp3FEXE3ag', 'wmIub4WwAA8nMLkNSv-isjQp3FEXE3ag', 'PRIVATE_CHAT', 'AUTO_REPLY', '价格', 'SUCCESS', '您好，当前产品价格请以客服最新报价为准，您也可以留下需求，我们会尽快跟进。', NULL, '2026-05-23 15:58:11', '2026-05-23 15:58:11', 0);
INSERT INTO `operation_log` VALUES (16, 'wmIub4WwAA8nMLkNSv-isjQp3FEXE3ag', 'wmIub4WwAA8nMLkNSv-isjQp3FEXE3ag', 'PRIVATE_CHAT', 'GROUP_INVITE', '进群', 'SUCCESS', '欢迎加入学习交流群，请先阅读群公告。扫码入群\n请点击以下链接或扫码进群：https://wccprint.top:8443/uploads/qrcodes/2026-05-23/0433bf15-8bf1-4e54-9325-7da675f5bdce.png', NULL, '2026-05-23 15:58:23', '2026-05-23 15:58:23', 0);
INSERT INTO `operation_log` VALUES (17, 'wmIub4WwAA8nMLkNSv-isjQp3FEXE3ag', 'wmIub4WwAA8nMLkNSv-isjQp3FEXE3ag', 'PRIVATE_CHAT', 'AUTO_REPLY', '价格', 'SUCCESS', '您好，当前产品价格请以客服最新报价为准，您也可以留下需求，我们会尽快跟进。', NULL, '2026-05-23 15:59:29', '2026-05-23 15:59:29', 0);
INSERT INTO `operation_log` VALUES (18, 'wmIub4WwAAEjHtZ3FRb2tIEtvFw_aCOA', 'wmIub4WwAAEjHtZ3FRb2tIEtvFw_aCOA', 'PRIVATE_CHAT', 'FALLBACK', '你好', 'SUCCESS', '暂无相关解答，可私信客服咨询详情', NULL, '2026-05-23 15:59:51', '2026-05-23 15:59:51', 0);
INSERT INTO `operation_log` VALUES (19, 'wmIub4WwAAEjHtZ3FRb2tIEtvFw_aCOA', 'wmIub4WwAAEjHtZ3FRb2tIEtvFw_aCOA', 'PRIVATE_CHAT', 'GROUP_INVITE', '进群', 'SUCCESS', '欢迎加入学习交流群，请先阅读群公告。扫码入群\n请点击以下链接或扫码进群：https://wccprint.top:8443/uploads/qrcodes/2026-05-23/0433bf15-8bf1-4e54-9325-7da675f5bdce.png', NULL, '2026-05-23 15:59:53', '2026-05-23 15:59:53', 0);
INSERT INTO `operation_log` VALUES (20, 'wmIub4WwAA1Zb2vZd4WVXXSNiKKHpPQw', 'wmIub4WwAA1Zb2vZd4WVXXSNiKKHpPQw', 'PRIVATE_CHAT', 'AUTO_REPLY', '价格', 'SUCCESS', '您好，当前产品价格请以客服最新报价为准，您也可以留下需求，我们会尽快跟进。', NULL, '2026-05-23 16:03:03', '2026-05-23 16:03:03', 0);
INSERT INTO `operation_log` VALUES (21, 'wmIub4WwAA1Zb2vZd4WVXXSNiKKHpPQw', 'wmIub4WwAA1Zb2vZd4WVXXSNiKKHpPQw', 'PRIVATE_CHAT', 'AUTO_REPLY', '价格', 'SUCCESS', '您好，当前产品价格请以客服最新报价为准，您也可以留下需求，我们会尽快跟进。', NULL, '2026-05-23 16:03:19', '2026-05-23 16:03:19', 0);
INSERT INTO `operation_log` VALUES (22, 'wmIub4WwAAEjHtZ3FRb2tIEtvFw_aCOA', 'wmIub4WwAAEjHtZ3FRb2tIEtvFw_aCOA', 'PRIVATE_CHAT', 'GROUP_INVITE', '进群', 'SKIPPED', '您已在群内，无需重复进群', 'Duplicate invite blocked', '2026-05-23 16:04:06', '2026-05-23 16:04:06', 0);
INSERT INTO `operation_log` VALUES (23, 'wmIub4WwAAEjHtZ3FRb2tIEtvFw_aCOA', 'wmIub4WwAAEjHtZ3FRb2tIEtvFw_aCOA', 'PRIVATE_CHAT', 'GROUP_INVITE', '进群', 'SKIPPED', '您已在群内，无需重复进群', 'Duplicate invite blocked', '2026-05-23 16:04:29', '2026-05-23 16:04:29', 0);
INSERT INTO `operation_log` VALUES (24, 'wmIub4WwAAEjHtZ3FRb2tIEtvFw_aCOA', 'wmIub4WwAAEjHtZ3FRb2tIEtvFw_aCOA', 'PRIVATE_CHAT', 'GROUP_INVITE', '进群', 'SKIPPED', '您已在群内，无需重复进群', 'Duplicate invite blocked', '2026-05-23 16:04:34', '2026-05-23 16:04:34', 0);
INSERT INTO `operation_log` VALUES (25, 'wmIub4WwAAEjHtZ3FRb2tIEtvFw_aCOA', 'wmIub4WwAAEjHtZ3FRb2tIEtvFw_aCOA', 'PRIVATE_CHAT', 'GROUP_INVITE', '进群', 'SUCCESS', '欢迎加入备用交流群，请先阅读群公告。\n请点击以下链接或扫码进群：https://wccprint.top:8443/uploads/qrcodes/2026-05-23/47355231-48c9-46f4-a31a-5e5bc2185386.png', NULL, '2026-05-23 16:04:52', '2026-05-23 16:04:52', 0);

-- ----------------------------
-- Table structure for reply_content
-- ----------------------------
DROP TABLE IF EXISTS `reply_content`;
CREATE TABLE `reply_content`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `keyword_rule_id` bigint NOT NULL,
  `content_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'TEXT',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `sort_order` int NOT NULL DEFAULT 0,
  `status` tinyint NOT NULL DEFAULT 1,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_keyword_rule`(`keyword_rule_id` ASC, `status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of reply_content
-- ----------------------------
INSERT INTO `reply_content` VALUES (1, 2, 'TEXT', '您好，当前产品价格请以客服最新报价为准，您也可以留下需求，我们会尽快跟进。', 0, 1, '2026-05-22 19:06:20', '2026-05-22 19:06:20', 0);
INSERT INTO `reply_content` VALUES (2, 3, 'TEXT', '资料领取链接：https://example.com/materials', 0, 1, '2026-05-22 19:06:20', '2026-05-22 19:06:20', 0);

-- ----------------------------
-- Table structure for risk_config
-- ----------------------------
DROP TABLE IF EXISTS `risk_config`;
CREATE TABLE `risk_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `config_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `config_desc` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_config_key`(`config_key` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of risk_config
-- ----------------------------
INSERT INTO `risk_config` VALUES (1, 'automation.paused', 'false', '是否暂停所有自动化操作', '2026-05-22 19:06:20', '2026-05-22 19:06:20', 0);
INSERT INTO `risk_config` VALUES (2, 'delay.min.seconds', '0.1', '自动动作最小随机延迟秒数', '2026-05-22 19:06:20', '2026-05-22 19:06:20', 0);
INSERT INTO `risk_config` VALUES (3, 'delay.max.seconds', '5', '自动动作最大随机延迟秒数', '2026-05-22 19:06:20', '2026-05-22 19:06:20', 0);
INSERT INTO `risk_config` VALUES (4, 'daily.reply.max', '500', '每日最大自动回复次数', '2026-05-22 19:06:20', '2026-05-22 19:06:20', 0);
INSERT INTO `risk_config` VALUES (5, 'daily.invite.max', '100', '每日最大自动拉群次数', '2026-05-22 19:06:20', '2026-05-22 19:06:20', 0);
INSERT INTO `risk_config` VALUES (6, 'user.minute.max', '5', '单用户每分钟最大触发次数', '2026-05-22 19:06:20', '2026-05-22 19:06:20', 0);
INSERT INTO `risk_config` VALUES (7, 'sensitive.words', '诈骗,灰产,违法', '敏感词，英文逗号分隔', '2026-05-22 19:06:20', '2026-05-22 19:06:20', 0);

-- ----------------------------
-- Table structure for user_group_record
-- ----------------------------
DROP TABLE IF EXISTS `user_group_record`;
CREATE TABLE `user_group_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `external_user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `group_id` bigint NOT NULL,
  `status` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `invite_time` datetime NULL DEFAULT NULL,
  `join_time` datetime NULL DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_group`(`external_user_id` ASC, `group_id` ASC, `status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_group_record
-- ----------------------------
INSERT INTO `user_group_record` VALUES (1, 'demo-user', 1, 'INVITED', '2026-05-22 20:01:20', NULL, '2026-05-22 20:01:20', '2026-05-22 20:01:20', 0);
INSERT INTO `user_group_record` VALUES (2, 'LiZhaoFu', 1, 'INVITED', '2026-05-23 12:44:03', NULL, '2026-05-23 12:44:03', '2026-05-23 12:44:03', 0);
INSERT INTO `user_group_record` VALUES (3, 'wmIub4WwAAvFHkyK_CB_OMGcLPUkx50A', 1, 'INVITED', '2026-05-23 13:55:15', NULL, '2026-05-23 13:55:15', '2026-05-23 13:55:15', 0);
INSERT INTO `user_group_record` VALUES (4, 'wmIub4WwAA1Zb2vZd4WVXXSNiKKHpPQw', 1, 'INVITED', '2026-05-23 15:27:13', NULL, '2026-05-23 15:27:13', '2026-05-23 15:27:13', 0);
INSERT INTO `user_group_record` VALUES (5, 'wmIub4WwAA1Zb2vZd4WVXXSNiKKHpPQw', 2, 'INVITED', '2026-05-23 15:45:55', NULL, '2026-05-23 15:45:55', '2026-05-23 15:45:55', 0);
INSERT INTO `user_group_record` VALUES (6, 'wmIub4WwAA8nMLkNSv-isjQp3FEXE3ag', 1, 'INVITED', '2026-05-23 15:58:23', NULL, '2026-05-23 15:58:23', '2026-05-23 15:58:23', 0);
INSERT INTO `user_group_record` VALUES (7, 'wmIub4WwAAEjHtZ3FRb2tIEtvFw_aCOA', 1, 'INVITED', '2026-05-23 15:59:53', NULL, '2026-05-23 15:59:53', '2026-05-23 15:59:53', 0);
INSERT INTO `user_group_record` VALUES (8, 'wmIub4WwAAEjHtZ3FRb2tIEtvFw_aCOA', 2, 'INVITED', '2026-05-23 16:04:52', NULL, '2026-05-23 16:04:52', '2026-05-23 16:04:52', 0);

-- ----------------------------
-- Table structure for wechat_group
-- ----------------------------
DROP TABLE IF EXISTS `wechat_group`;
CREATE TABLE `wechat_group`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `external_group_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `group_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `member_limit` int NOT NULL DEFAULT 500,
  `current_members` int NOT NULL DEFAULT 0,
  `welcome_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `qr_code_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `status` tinyint NOT NULL DEFAULT 1,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_external_group_id`(`external_group_id` ASC) USING BTREE,
  INDEX `idx_group_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wechat_group
-- ----------------------------
INSERT INTO `wechat_group` VALUES (1, 'demo_group_001', '营销10086群', 500, 120, '欢迎加入学习交流群，请先阅读群公告。扫码入群', 'https://wccprint.top:8443/uploads/qrcodes/2026-05-23/0433bf15-8bf1-4e54-9325-7da675f5bdce.png', 0, '2026-05-22 19:06:20', '2026-05-22 19:06:20', 0);
INSERT INTO `wechat_group` VALUES (2, 'demo_group_002', '营销二群', 500, 0, '欢迎加入备用交流群，请先阅读群公告。', 'https://wccprint.top:8443/uploads/qrcodes/2026-05-23/47355231-48c9-46f4-a31a-5e5bc2185386.png', 1, '2026-05-22 19:06:20', '2026-05-22 19:06:20', 0);

SET FOREIGN_KEY_CHECKS = 1;
