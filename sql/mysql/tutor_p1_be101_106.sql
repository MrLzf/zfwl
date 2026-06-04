-- P1 BE-101 ~ BE-106: point recharge, invitation, and value services.

CREATE TABLE IF NOT EXISTS `tutor_point_package` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(64) NOT NULL COMMENT '积分包名称',
  `point` int NOT NULL COMMENT '基础积分',
  `bonus_point` int NOT NULL DEFAULT 0 COMMENT '赠送积分',
  `price` int NOT NULL COMMENT '价格，单位：分',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家教积分包配置';

CREATE TABLE IF NOT EXISTS `tutor_point_recharge_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint NOT NULL COMMENT '用户编号',
  `package_id` bigint NOT NULL COMMENT '积分包编号',
  `package_name` varchar(64) NOT NULL COMMENT '积分包名称',
  `point` int NOT NULL COMMENT '基础积分',
  `bonus_point` int NOT NULL DEFAULT 0 COMMENT '赠送积分',
  `total_point` int NOT NULL COMMENT '到账积分',
  `price` int NOT NULL COMMENT '支付金额，单位：分',
  `status` tinyint NOT NULL COMMENT '订单状态',
  `merchant_order_id` varchar(64) DEFAULT NULL COMMENT '商户订单号',
  `pay_order_id` bigint DEFAULT NULL COMMENT '支付订单编号',
  `pay_channel_code` varchar(32) DEFAULT NULL COMMENT '支付渠道',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `expire_time` datetime NOT NULL COMMENT '过期时间',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_merchant_order_id` (`merchant_order_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家教积分充值订单';

CREATE TABLE IF NOT EXISTS `tutor_invite_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint NOT NULL COMMENT '用户编号',
  `invite_code` varchar(16) NOT NULL COMMENT '邀请码',
  `invite_link` varchar(255) NOT NULL COMMENT '邀请链接',
  `link_expire_time` datetime NOT NULL COMMENT '链接过期时间',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  UNIQUE KEY `uk_invite_code` (`invite_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家教用户邀请码';

CREATE TABLE IF NOT EXISTS `tutor_invite_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `inviter_user_id` bigint NOT NULL COMMENT '邀请人',
  `invitee_user_id` bigint NOT NULL COMMENT '被邀请人',
  `invite_code` varchar(16) NOT NULL COMMENT '邀请码',
  `device_id` varchar(128) DEFAULT NULL COMMENT '设备标识',
  `ip` varchar(64) DEFAULT NULL COMMENT '注册 IP',
  `reward_point` int NOT NULL COMMENT '奖励积分',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_invitee_user_id` (`invitee_user_id`),
  KEY `idx_device_id` (`device_id`),
  KEY `idx_ip` (`ip`),
  KEY `idx_inviter_create_time` (`inviter_user_id`, `create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家教邀请奖励记录';

CREATE TABLE IF NOT EXISTS `tutor_value_service_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `service_type` varchar(16) NOT NULL COMMENT '服务类型：boost/urgent/top',
  `target_type` varchar(16) NOT NULL COMMENT '目标类型：demand/resume',
  `point_price` int NOT NULL COMMENT '积分价格',
  `duration_hours` int NOT NULL COMMENT '生效时长，小时',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`),
  KEY `idx_target_type` (`target_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家教增值服务配置';

CREATE TABLE IF NOT EXISTS `tutor_value_service_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint NOT NULL COMMENT '用户编号',
  `config_id` bigint NOT NULL COMMENT '配置编号',
  `service_type` varchar(16) NOT NULL COMMENT '服务类型',
  `target_type` varchar(16) NOT NULL COMMENT '目标类型',
  `target_id` bigint NOT NULL COMMENT '目标编号',
  `point_price` int NOT NULL COMMENT '积分价格',
  `effect_start_time` datetime NOT NULL COMMENT '生效时间',
  `effect_end_time` datetime NOT NULL COMMENT '失效时间',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '订单状态',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_target` (`target_type`, `target_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家教增值服务订单';

INSERT INTO `tutor_point_package` (`id`, `name`, `point`, `bonus_point`, `price`, `status`, `sort`)
VALUES
  (101, '100积分包', 100, 0, 990, 0, 10),
  (102, '300积分包', 300, 30, 2790, 0, 20),
  (103, '600积分包', 600, 90, 4990, 0, 30)
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`), `point` = VALUES(`point`),
  `bonus_point` = VALUES(`bonus_point`), `price` = VALUES(`price`), `status` = VALUES(`status`);

INSERT INTO `tutor_value_service_config` (`id`, `service_type`, `target_type`, `point_price`, `duration_hours`, `status`)
VALUES
  (101, 'boost', 'demand', 10, 24, 0),
  (102, 'urgent', 'demand', 20, 24, 0),
  (103, 'top', 'demand', 30, 24, 0),
  (201, 'boost', 'resume', 10, 24, 0),
  (202, 'urgent', 'resume', 20, 24, 0),
  (203, 'top', 'resume', 30, 24, 0)
ON DUPLICATE KEY UPDATE `service_type` = VALUES(`service_type`), `target_type` = VALUES(`target_type`),
  `point_price` = VALUES(`point_price`), `duration_hours` = VALUES(`duration_hours`), `status` = VALUES(`status`);
