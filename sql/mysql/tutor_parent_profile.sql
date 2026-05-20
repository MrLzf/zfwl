-- 家长资料表：需先执行 tutor_profile.sql

CREATE TABLE IF NOT EXISTS `tutor_parent_profile` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '家长资料编号',
  `user_id` bigint NOT NULL COMMENT '会员用户编号',
  `profile_id` bigint NOT NULL COMMENT '家教用户档案编号',
  `child_grade` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '孩子年级',
  `subjects` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '辅导科目，多个用逗号分隔',
  `budget_min` int NOT NULL DEFAULT 0 COMMENT '每小时最低预算',
  `budget_max` int NOT NULL DEFAULT 0 COMMENT '每小时最高预算',
  `teach_mode` tinyint NOT NULL COMMENT '授课模式：1 上门，2 在线，3 均可',
  `remark` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '补充说明',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_tutor_parent_profile_user` (`user_id`, `deleted`) USING BTREE,
  KEY `idx_tutor_parent_profile_profile` (`profile_id`) USING BTREE,
  KEY `idx_tutor_parent_profile_grade_mode` (`child_grade`, `teach_mode`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '家教家长资料表';
