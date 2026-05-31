CREATE TABLE IF NOT EXISTS `tutor_point_reward_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '奖励记录编号',
  `user_id` bigint NOT NULL COMMENT '会员用户编号',
  `task_type` varchar(32) NOT NULL COMMENT '任务类型',
  `biz_id` varchar(64) NOT NULL COMMENT '幂等业务编号',
  `point` int NOT NULL COMMENT '奖励积分',
  `remark` varchar(255) NOT NULL COMMENT '奖励说明',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_tutor_point_reward` (`user_id`, `task_type`, `biz_id`, `deleted`) USING BTREE
) ENGINE=InnoDB COMMENT='家教积分任务奖励记录表';
