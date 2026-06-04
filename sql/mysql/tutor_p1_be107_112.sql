-- 家教 P1 BE-107~BE-112：VIP、订阅消息、客服留言、投诉、内容安全、评价标签

CREATE TABLE IF NOT EXISTS `tutor_vip_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint NOT NULL COMMENT '用户编号',
  `config_id` bigint NOT NULL COMMENT 'VIP 配置编号',
  `point_price` int NOT NULL COMMENT '积分价格',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `monthly_gift_point` int NOT NULL DEFAULT 30 COMMENT '每月赠送积分',
  `last_gift_time` datetime DEFAULT NULL COMMENT '最近赠送时间',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态：0 生效，1 失效',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`),
  KEY `idx_tutor_vip_user_time` (`user_id`, `status`, `start_time`, `end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='家教 VIP 记录';

CREATE TABLE IF NOT EXISTS `tutor_subscribe_setting` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint NOT NULL COMMENT '用户编号',
  `notice_type` varchar(32) NOT NULL COMMENT '通知类型',
  `enabled` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否开启',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tutor_subscribe_setting` (`user_id`, `notice_type`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='家教订阅消息设置';

CREATE TABLE IF NOT EXISTS `tutor_subscribe_message_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint NOT NULL COMMENT '用户编号',
  `notice_type` varchar(32) NOT NULL COMMENT '通知类型',
  `title` varchar(128) NOT NULL COMMENT '标题',
  `content` varchar(512) DEFAULT NULL COMMENT '内容',
  `biz_id` varchar(128) DEFAULT NULL COMMENT '业务编号',
  `target_type` varchar(32) DEFAULT NULL COMMENT '目标类型',
  `target_id` bigint DEFAULT NULL COMMENT '目标编号',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态：0 待发送，10 已发送，20 失败，30 用户未订阅',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`),
  KEY `idx_tutor_subscribe_record_user` (`user_id`, `notice_type`, `status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='家教订阅消息发送记录';

CREATE TABLE IF NOT EXISTS `tutor_chat_message` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint NOT NULL COMMENT '发送人',
  `receiver_user_id` bigint NOT NULL COMMENT '接收人',
  `message_type` varchar(16) NOT NULL COMMENT '消息类型：text/image',
  `content` varchar(1000) DEFAULT NULL COMMENT '文字内容',
  `image_url` varchar(512) DEFAULT NULL COMMENT '图片地址',
  `read_status` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否已读',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`),
  KEY `idx_tutor_chat_pair_time` (`user_id`, `receiver_user_id`, `create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='家教客服留言消息';

CREATE TABLE IF NOT EXISTS `tutor_complaint` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint NOT NULL COMMENT '举报人',
  `target_type` varchar(32) NOT NULL COMMENT '目标类型',
  `target_id` bigint NOT NULL COMMENT '目标编号',
  `target_user_id` bigint NOT NULL COMMENT '被举报用户',
  `reason_type` varchar(64) NOT NULL COMMENT '举报原因',
  `content` varchar(1000) DEFAULT NULL COMMENT '举报说明',
  `image_urls` varchar(2000) DEFAULT NULL COMMENT '图片证据',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态：0 待处理，10 已处理，20 已驳回',
  `handler_id` bigint DEFAULT NULL COMMENT '处理人',
  `handle_result` varchar(1000) DEFAULT NULL COMMENT '处理结果',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`),
  KEY `idx_tutor_complaint_target` (`target_type`, `target_id`),
  KEY `idx_tutor_complaint_status` (`status`, `create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='家教举报投诉';

INSERT INTO `tutor_value_service_config` (`service_type`, `target_type`, `point_price`, `duration_hours`, `status`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
SELECT 'vip', 'user', 199, 720, 0, 'admin', NOW(), 'admin', NOW(), b'0', 0
WHERE NOT EXISTS (SELECT 1 FROM `tutor_value_service_config` WHERE `service_type` = 'vip' AND `target_type` = 'user' AND `deleted` = b'0');
