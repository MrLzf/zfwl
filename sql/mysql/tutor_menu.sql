-- 家教模块菜单与权限初始化
-- 可重复执行：通过 path / permission 判断是否已存在，不依赖固定菜单 id。

-- 顶级目录：家教管理
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`,
                           `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
                           `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT '家教管理', '', 1, 260, 0, '/tutor', 'ep:school', '', '',
       0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'
WHERE NOT EXISTS (
    SELECT 1 FROM `system_menu`
    WHERE `parent_id` = 0 AND `path` = '/tutor' AND `deleted` = b'0'
);

SET @tutor_parent_id := (
    SELECT `id` FROM `system_menu`
    WHERE `parent_id` = 0 AND `path` = '/tutor' AND `deleted` = b'0'
    ORDER BY `id` ASC LIMIT 1
);

-- 二级菜单：城市管理
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`,
                           `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
                           `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT '城市管理', 'tutor:city:query', 2, 90, @tutor_parent_id, 'city', 'ep:location',
       'tutor/city/index', 'TutorCity', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'
WHERE @tutor_parent_id IS NOT NULL
  AND NOT EXISTS (
      SELECT 1 FROM `system_menu`
      WHERE `parent_id` = @tutor_parent_id AND `path` = 'city' AND `deleted` = b'0'
  );

SET @tutor_city_menu_id := (
    SELECT `id` FROM `system_menu`
    WHERE `parent_id` = @tutor_parent_id AND `path` = 'city' AND `deleted` = b'0'
    ORDER BY `id` ASC LIMIT 1
);

-- 按钮权限：城市查询
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`,
                           `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
                           `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT '城市查询', 'tutor:city:query', 3, 1, @tutor_city_menu_id, '', '', '', NULL,
       0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'
WHERE @tutor_city_menu_id IS NOT NULL
  AND NOT EXISTS (
      SELECT 1 FROM `system_menu`
      WHERE `parent_id` = @tutor_city_menu_id AND `permission` = 'tutor:city:query' AND `deleted` = b'0'
  );

-- 按钮权限：城市更新
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`,
                           `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
                           `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT '城市更新', 'tutor:city:update', 3, 2, @tutor_city_menu_id, '', '', '', NULL,
       0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'
WHERE @tutor_city_menu_id IS NOT NULL
  AND NOT EXISTS (
      SELECT 1 FROM `system_menu`
      WHERE `parent_id` = @tutor_city_menu_id AND `permission` = 'tutor:city:update' AND `deleted` = b'0'
  );

-- 二级菜单：教师认证审核
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`,
                           `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
                           `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT '教师认证审核', 'tutor:certification:query', 2, 80, @tutor_parent_id, 'certification', 'ep:checked',
       'tutor/certification/index', 'TutorCertification', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'
WHERE @tutor_parent_id IS NOT NULL
  AND NOT EXISTS (
      SELECT 1 FROM `system_menu`
      WHERE `parent_id` = @tutor_parent_id AND `path` = 'certification' AND `deleted` = b'0'
  );

SET @tutor_certification_menu_id := (
    SELECT `id` FROM `system_menu`
    WHERE `parent_id` = @tutor_parent_id AND `path` = 'certification' AND `deleted` = b'0'
    ORDER BY `id` ASC LIMIT 1
);

-- 按钮权限：认证查询
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`,
                           `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
                           `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT '认证查询', 'tutor:certification:query', 3, 1, @tutor_certification_menu_id, '', '', '', NULL,
       0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'
WHERE @tutor_certification_menu_id IS NOT NULL
  AND NOT EXISTS (
      SELECT 1 FROM `system_menu`
      WHERE `parent_id` = @tutor_certification_menu_id AND `permission` = 'tutor:certification:query' AND `deleted` = b'0'
  );

-- 按钮权限：认证审核
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`,
                           `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
                           `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT '认证审核', 'tutor:certification:audit', 3, 2, @tutor_certification_menu_id, '', '', '', NULL,
       0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'
WHERE @tutor_certification_menu_id IS NOT NULL
  AND NOT EXISTS (
      SELECT 1 FROM `system_menu`
      WHERE `parent_id` = @tutor_certification_menu_id AND `permission` = 'tutor:certification:audit' AND `deleted` = b'0'
  );
