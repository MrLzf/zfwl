-- 家教互动闭环表：需先执行 tutor_publish.sql

CREATE TABLE IF NOT EXISTS `tutor_favorite` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '收藏编号',
  `user_id` bigint NOT NULL COMMENT '会员用户编号',
  `target_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '目标类型：demand 家长需求，resume 教师简历',
  `target_id` bigint NOT NULL COMMENT '目标编号',
  `target_user_id` bigint NOT NULL COMMENT '目标发布用户编号',
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '目标标题快照',
  `city_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '城市编码',
  `city_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '城市名称',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_tutor_favorite_user_target` (`user_id`, `target_type`, `target_id`, `deleted`) USING BTREE,
  KEY `idx_tutor_favorite_user_time` (`user_id`, `create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '家教收藏表';

CREATE TABLE IF NOT EXISTS `tutor_contact_view_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '联系查看记录编号',
  `viewer_user_id` bigint NOT NULL COMMENT '查看人用户编号',
  `target_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '目标类型',
  `target_id` bigint NOT NULL COMMENT '目标编号',
  `target_user_id` bigint NOT NULL COMMENT '被查看用户编号',
  `point_cost` int NOT NULL DEFAULT 0 COMMENT '扣除积分',
  `free_reuse_until` datetime NOT NULL COMMENT '免费复看截止时间',
  `viewed_mobile` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否查看手机号',
  `viewed_wechat` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否查看微信号',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_tutor_contact_view_reuse` (`viewer_user_id`, `target_type`, `target_id`, `free_reuse_until`) USING BTREE,
  KEY `idx_tutor_contact_view_target` (`target_user_id`, `create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '家教联系方式查看记录表';

CREATE TABLE IF NOT EXISTS `tutor_match_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '匹配记录编号',
  `demand_id` bigint NULL DEFAULT NULL COMMENT '家长需求编号',
  `resume_id` bigint NULL DEFAULT NULL COMMENT '教师简历编号',
  `parent_user_id` bigint NOT NULL COMMENT '家长用户编号',
  `teacher_user_id` bigint NOT NULL COMMENT '教师用户编号',
  `source` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '来源',
  `status` tinyint NOT NULL DEFAULT 10 COMMENT '状态：10 已交换联系方式，20 家长确认，30 教师确认，40 双方确认，50 取消',
  `parent_confirm_time` datetime NULL DEFAULT NULL COMMENT '家长确认时间',
  `teacher_confirm_time` datetime NULL DEFAULT NULL COMMENT '教师确认时间',
  `matched_time` datetime NULL DEFAULT NULL COMMENT '双方确认时间',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_tutor_match_parent` (`parent_user_id`, `create_time`) USING BTREE,
  KEY `idx_tutor_match_teacher` (`teacher_user_id`, `create_time`) USING BTREE,
  KEY `idx_tutor_match_demand_teacher` (`demand_id`, `teacher_user_id`) USING BTREE,
  KEY `idx_tutor_match_resume_parent` (`resume_id`, `parent_user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '家教匹配记录表';

CREATE TABLE IF NOT EXISTS `tutor_review` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评价编号',
  `match_id` bigint NOT NULL COMMENT '匹配记录编号',
  `reviewer_user_id` bigint NOT NULL COMMENT '评价人用户编号',
  `target_user_id` bigint NOT NULL COMMENT '被评价用户编号',
  `rating` tinyint NOT NULL COMMENT '评分 1-5',
  `tags` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标签',
  `content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '评价内容',
  `anonymous_display` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否匿名展示',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态：0 正常，1 隐藏',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_tutor_review_match_reviewer` (`match_id`, `reviewer_user_id`, `deleted`) USING BTREE,
  KEY `idx_tutor_review_target` (`target_user_id`, `status`, `create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '家教评价表';
