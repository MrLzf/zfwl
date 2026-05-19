-- ----------------------------
-- 家教供需匹配平台 P0 数据库初始化
-- ----------------------------

-- ----------------------------
-- Table structure for tutor_city
-- ----------------------------
DROP TABLE IF EXISTS `tutor_city`;
CREATE TABLE `tutor_city` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '城市编号',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '城市名称',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '城市编码',
  `pinyin` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '拼音',
  `province` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '省份',
  `opened` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否开通',
  `hot` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否热门城市',
  `service_config` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '城市运营配置 JSON',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态（0 正常 1 停用）',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_tutor_city_code` (`code`) USING BTREE,
  KEY `idx_tutor_city_opened_hot_sort` (`opened`, `hot`, `sort`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '家教城市表';

-- ----------------------------
-- Records of tutor_city
-- ----------------------------
BEGIN;
INSERT INTO `tutor_city` (`id`, `name`, `code`, `pinyin`, `province`, `opened`, `hot`, `service_config`, `sort`, `status`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(1, '北京', '110100', 'beijing', '北京市', b'1', b'1', '{"defaultRadiusKm":10,"contactPointCost":10}', 10, 0, 'admin', NOW(), 'admin', NOW(), b'0'),
(2, '上海', '310100', 'shanghai', '上海市', b'1', b'1', '{"defaultRadiusKm":10,"contactPointCost":10}', 20, 0, 'admin', NOW(), 'admin', NOW(), b'0'),
(3, '广州', '440100', 'guangzhou', '广东省', b'1', b'1', '{"defaultRadiusKm":10,"contactPointCost":10}', 30, 0, 'admin', NOW(), 'admin', NOW(), b'0'),
(4, '深圳', '440300', 'shenzhen', '广东省', b'1', b'1', '{"defaultRadiusKm":10,"contactPointCost":10}', 40, 0, 'admin', NOW(), 'admin', NOW(), b'0'),
(5, '杭州', '330100', 'hangzhou', '浙江省', b'1', b'1', '{"defaultRadiusKm":10,"contactPointCost":10}', 50, 0, 'admin', NOW(), 'admin', NOW(), b'0'),
(6, '成都', '510100', 'chengdu', '四川省', b'1', b'1', '{"defaultRadiusKm":10,"contactPointCost":10}', 60, 0, 'admin', NOW(), 'admin', NOW(), b'0'),
(7, '武汉', '420100', 'wuhan', '湖北省', b'1', b'1', '{"defaultRadiusKm":10,"contactPointCost":10}', 70, 0, 'admin', NOW(), 'admin', NOW(), b'0'),
(8, '西安', '610100', 'xian', '陕西省', b'1', b'1', '{"defaultRadiusKm":10,"contactPointCost":10}', 80, 0, 'admin', NOW(), 'admin', NOW(), b'0');
COMMIT;
