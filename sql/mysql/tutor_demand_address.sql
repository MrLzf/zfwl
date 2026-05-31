ALTER TABLE `tutor_demand`
  ADD COLUMN `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL
  COMMENT '详细上课地址' AFTER `teach_mode`;
