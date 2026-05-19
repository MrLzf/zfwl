-- ----------------------------
-- 家教用户档案表：可在已执行 tutor.sql 后单独执行
-- ----------------------------
CREATE TABLE IF NOT EXISTS `tutor_user_profile` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '档案编号',
  `user_id` bigint NOT NULL COMMENT '会员用户编号',
  `role` tinyint NOT NULL COMMENT '身份：1 家长，2 教师',
  `city_id` bigint NOT NULL COMMENT '当前城市编号',
  `city_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '当前城市编码',
  `city_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '当前城市名称',
  `longitude` decimal(10,6) NULL DEFAULT NULL COMMENT '当前定位经度',
  `latitude` decimal(10,6) NULL DEFAULT NULL COMMENT '当前定位纬度',
  `location_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '当前定位地址',
  `location_time` datetime NULL DEFAULT NULL COMMENT '最近定位时间',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态（0 正常 1 停用）',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_tutor_user_profile_user` (`user_id`, `deleted`) USING BTREE,
  KEY `idx_tutor_user_profile_city_role` (`city_code`, `role`, `status`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '家教用户档案表';
