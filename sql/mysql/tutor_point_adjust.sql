-- 家教后台积分调整记录表
-- 用于记录 ADM-011 后台积分调整的操作人和备注；积分余额与会员积分流水仍复用 member_point_record。

CREATE TABLE IF NOT EXISTS `tutor_point_adjust_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '积分调整记录编号',
  `user_id` bigint NOT NULL COMMENT '会员用户编号',
  `point` int NOT NULL COMMENT '变动积分，正数增加，负数扣减',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '调整备注',
  `operator_id` bigint NOT NULL COMMENT '操作管理员编号',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_tutor_point_adjust_user` (`user_id`, `create_time`) USING BTREE,
  KEY `idx_tutor_point_adjust_operator` (`operator_id`, `create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '家教后台积分调整记录表';
