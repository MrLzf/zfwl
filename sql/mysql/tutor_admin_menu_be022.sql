-- BE-022 家教管理端菜单与权限初始化
-- 可重复执行：通过 path / permission 判断是否已存在。

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

INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`,
                           `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
                           `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT '数据概览', 'tutor:dashboard:query', 2, 1, @tutor_parent_id, 'dashboard', 'ep:data-line',
       'tutor/dashboard/index', 'TutorDashboard', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'
WHERE @tutor_parent_id IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM `system_menu` WHERE `parent_id` = @tutor_parent_id AND `path` = 'dashboard' AND `deleted` = b'0');

INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`,
                           `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
                           `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT '用户档案', 'tutor:profile:query', 2, 10, @tutor_parent_id, 'profiles', 'ep:user',
       'tutor/profile/index', 'TutorProfile', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'
WHERE @tutor_parent_id IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM `system_menu` WHERE `parent_id` = @tutor_parent_id AND `path` = 'profiles' AND `deleted` = b'0');

INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`,
                           `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
                           `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT '认证审核', 'tutor:certification:query', 2, 15, @tutor_parent_id, 'certification', 'ep:checked',
       'tutor/certification/index', 'TutorCertification', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'
WHERE @tutor_parent_id IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM `system_menu` WHERE `parent_id` = @tutor_parent_id AND `path` = 'certification' AND `deleted` = b'0');

SET @tutor_certification_menu_id := (
    SELECT `id` FROM `system_menu`
    WHERE `parent_id` = @tutor_parent_id AND `path` = 'certification' AND `deleted` = b'0'
    ORDER BY `id` ASC LIMIT 1
);

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

SET @tutor_profile_menu_id := (
    SELECT `id` FROM `system_menu`
    WHERE `parent_id` = @tutor_parent_id AND `path` = 'profiles' AND `deleted` = b'0'
    ORDER BY `id` ASC LIMIT 1
);

INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`,
                           `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
                           `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT '积分调整', 'tutor:point:adjust', 3, 2, @tutor_profile_menu_id, '', '', '', NULL,
       0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'
WHERE @tutor_profile_menu_id IS NOT NULL
  AND NOT EXISTS (
      SELECT 1 FROM `system_menu`
      WHERE `parent_id` = @tutor_profile_menu_id AND `permission` = 'tutor:point:adjust' AND `deleted` = b'0'
  );

INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`,
                           `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
                           `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT '需求管理', 'tutor:demand:query', 2, 20, @tutor_parent_id, 'demand', 'ep:document',
       'tutor/demand/index', 'TutorDemand', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'
WHERE @tutor_parent_id IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM `system_menu` WHERE `parent_id` = @tutor_parent_id AND `path` = 'demand' AND `deleted` = b'0');

INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`,
                           `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
                           `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT '简历管理', 'tutor:resume:query', 2, 30, @tutor_parent_id, 'resume', 'ep:notebook',
       'tutor/resume/index', 'TutorResume', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'
WHERE @tutor_parent_id IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM `system_menu` WHERE `parent_id` = @tutor_parent_id AND `path` = 'resume' AND `deleted` = b'0');

INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`,
                           `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
                           `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT '联系记录', 'tutor:contact:query', 2, 40, @tutor_parent_id, 'contacts', 'ep:phone',
       'tutor/contact/index', 'TutorContact', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'
WHERE @tutor_parent_id IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM `system_menu` WHERE `parent_id` = @tutor_parent_id AND `path` = 'contacts' AND `deleted` = b'0');

INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`,
                           `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
                           `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT '匹配记录', 'tutor:match:query', 2, 50, @tutor_parent_id, 'matches', 'ep:connection',
       'tutor/match/index', 'TutorMatch', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'
WHERE @tutor_parent_id IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM `system_menu` WHERE `parent_id` = @tutor_parent_id AND `path` = 'matches' AND `deleted` = b'0');

INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`,
                           `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
                           `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT '评价管理', 'tutor:review:query', 2, 60, @tutor_parent_id, 'reviews', 'ep:star',
       'tutor/review/index', 'TutorReview', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'
WHERE @tutor_parent_id IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM `system_menu` WHERE `parent_id` = @tutor_parent_id AND `path` = 'reviews' AND `deleted` = b'0');

INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`,
                           `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
                           `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT '城市管理', 'tutor:city:query', 2, 90, @tutor_parent_id, 'city', 'ep:location',
       'tutor/city/index', 'TutorCity', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'
WHERE @tutor_parent_id IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM `system_menu` WHERE `parent_id` = @tutor_parent_id AND `path` = 'city' AND `deleted` = b'0');

SET @tutor_city_menu_id := (
    SELECT `id` FROM `system_menu`
    WHERE `parent_id` = @tutor_parent_id AND `path` = 'city' AND `deleted` = b'0'
    ORDER BY `id` ASC LIMIT 1
);

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

SET @tutor_review_menu_id := (
    SELECT `id` FROM `system_menu`
    WHERE `parent_id` = @tutor_parent_id AND `path` = 'reviews' AND `deleted` = b'0'
    ORDER BY `id` ASC LIMIT 1
);

INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`,
                           `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
                           `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT '评价状态更新', 'tutor:review:update-status', 3, 2, @tutor_review_menu_id, '', '', '', NULL,
       0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'
WHERE @tutor_review_menu_id IS NOT NULL
  AND NOT EXISTS (
      SELECT 1 FROM `system_menu`
      WHERE `parent_id` = @tutor_review_menu_id AND `permission` = 'tutor:review:update-status' AND `deleted` = b'0'
  );

SET @tutor_demand_menu_id := (
    SELECT `id` FROM `system_menu`
    WHERE `parent_id` = @tutor_parent_id AND `path` = 'demand' AND `deleted` = b'0'
    ORDER BY `id` ASC LIMIT 1
);

INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`,
                           `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
                           `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT '需求审核', 'tutor:demand:audit', 3, 2, @tutor_demand_menu_id, '', '', '', NULL,
       0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'
WHERE @tutor_demand_menu_id IS NOT NULL
  AND NOT EXISTS (
      SELECT 1 FROM `system_menu`
      WHERE `parent_id` = @tutor_demand_menu_id AND `permission` = 'tutor:demand:audit' AND `deleted` = b'0'
  );

INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`,
                           `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
                           `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT '需求下架', 'tutor:demand:offline', 3, 3, @tutor_demand_menu_id, '', '', '', NULL,
       0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'
WHERE @tutor_demand_menu_id IS NOT NULL
  AND NOT EXISTS (
      SELECT 1 FROM `system_menu`
      WHERE `parent_id` = @tutor_demand_menu_id AND `permission` = 'tutor:demand:offline' AND `deleted` = b'0'
  );

SET @tutor_resume_menu_id := (
    SELECT `id` FROM `system_menu`
    WHERE `parent_id` = @tutor_parent_id AND `path` = 'resume' AND `deleted` = b'0'
    ORDER BY `id` ASC LIMIT 1
);

INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`,
                           `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
                           `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT '简历审核', 'tutor:resume:audit', 3, 2, @tutor_resume_menu_id, '', '', '', NULL,
       0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'
WHERE @tutor_resume_menu_id IS NOT NULL
  AND NOT EXISTS (
      SELECT 1 FROM `system_menu`
      WHERE `parent_id` = @tutor_resume_menu_id AND `permission` = 'tutor:resume:audit' AND `deleted` = b'0'
  );

INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`,
                           `component_name`, `status`, `visible`, `keep_alive`, `always_show`,
                           `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT '简历下架', 'tutor:resume:offline', 3, 3, @tutor_resume_menu_id, '', '', '', NULL,
       0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'
WHERE @tutor_resume_menu_id IS NOT NULL
  AND NOT EXISTS (
      SELECT 1 FROM `system_menu`
      WHERE `parent_id` = @tutor_resume_menu_id AND `permission` = 'tutor:resume:offline' AND `deleted` = b'0'
  );
