-- P2 规模化运营：预约试课、担保交易、教师等级、推荐权重、城市规则、LBS 索引。

CREATE TABLE IF NOT EXISTS `tutor_trial_time_slot` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `teacher_user_id` bigint NOT NULL COMMENT '教师用户编号',
  `resume_id` bigint NULL COMMENT '关联教师简历',
  `start_time` datetime NOT NULL COMMENT '可预约开始时间',
  `end_time` datetime NOT NULL COMMENT '可预约结束时间',
  `capacity` int NOT NULL DEFAULT 1 COMMENT '可预约容量',
  `booked_count` int NOT NULL DEFAULT 0 COMMENT '已预约数量',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态：0 可用，1 停用',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_tutor_trial_time_slot_teacher_time` (`teacher_user_id`, `start_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='家教试课可预约时间';

CREATE TABLE IF NOT EXISTS `tutor_trial_appointment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `slot_id` bigint NULL COMMENT '预约时间编号',
  `resume_id` bigint NULL COMMENT '教师简历编号',
  `parent_user_id` bigint NOT NULL COMMENT '家长用户编号',
  `teacher_user_id` bigint NOT NULL COMMENT '教师用户编号',
  `start_time` datetime NOT NULL COMMENT '预约开始时间',
  `end_time` datetime NOT NULL COMMENT '预约结束时间',
  `contact_mobile_mask` varchar(32) DEFAULT NULL COMMENT '联系手机号脱敏',
  `remark` varchar(512) DEFAULT NULL COMMENT '预约备注',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态：0 待教师确认，1 教师已确认，2 家长已确认，3 双方确认，4 取消，5 异常',
  `parent_confirm_time` datetime NULL COMMENT '家长确认时间',
  `teacher_confirm_time` datetime NULL COMMENT '教师确认时间',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_tutor_trial_appointment_parent` (`parent_user_id`, `create_time`),
  KEY `idx_tutor_trial_appointment_teacher` (`teacher_user_id`, `start_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='家教试课预约';

CREATE TABLE IF NOT EXISTS `tutor_escrow_trade_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `appointment_id` bigint NULL COMMENT '试课预约编号',
  `parent_user_id` bigint NOT NULL COMMENT '家长用户编号',
  `teacher_user_id` bigint NOT NULL COMMENT '教师用户编号',
  `pay_order_id` bigint NULL COMMENT '支付订单编号',
  `amount` int NOT NULL COMMENT '担保金额，单位分',
  `platform_fee` int NOT NULL DEFAULT 0 COMMENT '平台服务费，单位分',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态：0 待支付，1 已担保，2 完成待释放，3 已释放，4 退款中，5 已退款，6 争议中，7 已关闭',
  `paid_time` datetime NULL COMMENT '支付时间',
  `complete_time` datetime NULL COMMENT '确认完成时间',
  `release_time` datetime NULL COMMENT '释放时间',
  `refund_reason` varchar(512) DEFAULT NULL COMMENT '退款/争议原因',
  `admin_remark` varchar(512) DEFAULT NULL COMMENT '后台处理备注',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_tutor_escrow_trade_parent` (`parent_user_id`, `create_time`),
  KEY `idx_tutor_escrow_trade_teacher` (`teacher_user_id`, `create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='家教担保交易订单';

CREATE TABLE IF NOT EXISTS `tutor_teacher_level_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `teacher_user_id` bigint NOT NULL COMMENT '教师用户编号',
  `level_code` varchar(32) NOT NULL COMMENT '等级编码',
  `level_name` varchar(64) NOT NULL COMMENT '等级名称',
  `score` int NOT NULL DEFAULT 0 COMMENT '等级分',
  `rating_avg` decimal(4,2) NOT NULL DEFAULT 0.00 COMMENT '平均评分',
  `match_count` int NOT NULL DEFAULT 0 COMMENT '接单量',
  `complaint_count` int NOT NULL DEFAULT 0 COMMENT '投诉量',
  `benefits` varchar(512) DEFAULT NULL COMMENT '等级权益',
  `calculated_time` datetime NOT NULL COMMENT '计算时间',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tutor_teacher_level_record_user` (`teacher_user_id`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='家教教师等级记录';

CREATE TABLE IF NOT EXISTS `tutor_recommend_weight_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `scene` varchar(32) NOT NULL COMMENT '场景：square_resume/square_demand',
  `city_code` varchar(32) DEFAULT NULL COMMENT '城市编码，空表示全局',
  `distance_weight` int NOT NULL DEFAULT 30 COMMENT '距离权重',
  `rating_weight` int NOT NULL DEFAULT 30 COMMENT '评分权重',
  `active_weight` int NOT NULL DEFAULT 20 COMMENT '活跃权重',
  `top_weight` int NOT NULL DEFAULT 20 COMMENT '置顶/加急权重',
  `ab_group` varchar(32) NOT NULL DEFAULT 'default' COMMENT 'AB 实验组',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tutor_recommend_weight_scene_city_group` (`scene`, `city_code`, `ab_group`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='家教智能推荐权重配置';

ALTER TABLE `tutor_teacher_resume`
  ADD COLUMN `teacher_level_code` varchar(32) NULL COMMENT '教师等级编码' AFTER `match_count`,
  ADD COLUMN `teacher_level_name` varchar(64) NULL COMMENT '教师等级名称' AFTER `teacher_level_code`,
  ADD COLUMN `geohash` varchar(16) NULL COMMENT '位置 geohash' AFTER `latitude`,
  ADD KEY `idx_tutor_teacher_resume_geohash` (`city_code`, `geohash`, `status`, `audit_status`);

ALTER TABLE `tutor_demand`
  ADD COLUMN `geohash` varchar(16) NULL COMMENT '位置 geohash' AFTER `latitude`,
  ADD KEY `idx_tutor_demand_geohash` (`city_code`, `geohash`, `status`, `audit_status`);
