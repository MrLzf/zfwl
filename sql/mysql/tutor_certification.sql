-- 教师认证表：需先执行 tutor_teacher_profile.sql

CREATE TABLE IF NOT EXISTS `tutor_certification` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '教师认证编号',
  `user_id` bigint NOT NULL COMMENT '会员用户编号',
  `teacher_profile_id` bigint NOT NULL COMMENT '教师资料编号',
  `real_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '真实姓名',
  `id_card_no_encrypt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '身份证号加密值',
  `id_card_no_mask` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '身份证号脱敏值',
  `education_file_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '学历证明 URL',
  `teacher_cert_file_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '教师资格证 URL',
  `status` tinyint NOT NULL DEFAULT 10 COMMENT '审核状态：10 待审核，20 通过，30 拒绝',
  `reject_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '拒绝原因',
  `auditor_id` bigint NULL DEFAULT NULL COMMENT '审核人',
  `audit_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_tutor_certification_user` (`user_id`, `deleted`) USING BTREE,
  KEY `idx_tutor_certification_status` (`status`, `create_time`) USING BTREE,
  KEY `idx_tutor_certification_teacher_profile` (`teacher_profile_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '家教教师认证表';
