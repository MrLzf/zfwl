-- 家教模块站内信模板初始化
-- 可重复执行：已有模板更新为最新内容，新模板按 code 补充。

INSERT INTO `system_notify_template` (`name`, `code`, `nickname`, `content`, `type`, `params`, `status`, `remark`,
                                      `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT '家教教师认证审核结果', 'tutor_certification_audit', '家教助手',
       '您的教师认证审核结果为：{status}。{reason}', 2, '["status","reason"]', 0, 'BE-021 家教基础站内通知',
       'admin', NOW(), 'admin', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM `system_notify_template` WHERE `code` = 'tutor_certification_audit' AND `deleted` = b'0');

INSERT INTO `system_notify_template` (`name`, `code`, `nickname`, `content`, `type`, `params`, `status`, `remark`,
                                      `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT '家教发布审核结果', 'tutor_publish_audit', '家教助手',
       '您的{publishType}《{title}》审核结果为：{status}。{reason}', 2, '["publishType","title","status","reason"]', 0, 'BE-021 家教基础站内通知',
       'admin', NOW(), 'admin', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM `system_notify_template` WHERE `code` = 'tutor_publish_audit' AND `deleted` = b'0');

INSERT INTO `system_notify_template` (`name`, `code`, `nickname`, `content`, `type`, `params`, `status`, `remark`,
                                      `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT '家教积分变动通知', 'tutor_point_changed', '家教助手',
       '{scene}积分变动：{point}。', 2, '["scene","point"]', 0, 'BE-021 家教基础站内通知',
       'admin', NOW(), 'admin', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM `system_notify_template` WHERE `code` = 'tutor_point_changed' AND `deleted` = b'0');

INSERT INTO `system_notify_template` (`name`, `code`, `nickname`, `content`, `type`, `params`, `status`, `remark`,
                                      `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT '家教匹配成功通知', 'tutor_match_success', '家教助手',
       '您有一条家教匹配已双方确认，匹配编号：{matchId}。', 2, '["matchId"]', 0, 'BE-021 家教基础站内通知',
       'admin', NOW(), 'admin', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM `system_notify_template` WHERE `code` = 'tutor_match_success' AND `deleted` = b'0');

INSERT INTO `system_notify_template` (`name`, `code`, `nickname`, `content`, `type`, `params`, `status`, `remark`,
                                      `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT '家教评价通知', 'tutor_review_created', '家教助手',
       '用户 {reviewerUserId} 给您提交了 {rating} 星评价。', 2, '["reviewerUserId","rating"]', 0, 'BE-021 家教基础站内通知',
       'admin', NOW(), 'admin', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM `system_notify_template` WHERE `code` = 'tutor_review_created' AND `deleted` = b'0');

UPDATE `system_notify_template`
SET `content` = '{title}积分变动：{point}。',
    `params` = '["title","point","totalPoint","category","action","bizId","targetType","targetId"]',
    `updater` = 'admin', `update_time` = NOW()
WHERE `code` = 'tutor_point_changed' AND `deleted` = b'0';

INSERT INTO `system_notify_template` (`name`, `code`, `nickname`, `content`, `type`, `params`, `status`, `remark`,
                                      `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT '家教联系方式查看通知', 'tutor_contact_viewer', '家教助手',
       '您已查看 {counterpartName} 在《{contentTitle}》中的联系方式。是否为复用：{reuse}。请注意核验身份并保护个人信息。', 2,
       '["category","action","bizId","targetType","targetId","counterpartUserId","counterpartName","contentTitle","reuse"]', 0, 'Task 1 家教联系站内通知',
       'admin', NOW(), 'admin', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM `system_notify_template` WHERE `code` = 'tutor_contact_viewer' AND `deleted` = b'0');

INSERT INTO `system_notify_template` (`name`, `code`, `nickname`, `content`, `type`, `params`, `status`, `remark`,
                                      `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT '家教联系方式被查看通知', 'tutor_contact_owner', '家教助手',
       '{counterpartName} 查看了您在《{contentTitle}》中的联系方式。是否为复用：{reuse}。请留意后续沟通并注意安全。', 2,
       '["category","action","bizId","targetType","targetId","counterpartUserId","counterpartName","contentTitle","reuse"]', 0, 'Task 1 家教联系站内通知',
       'admin', NOW(), 'admin', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM `system_notify_template` WHERE `code` = 'tutor_contact_owner' AND `deleted` = b'0');

UPDATE `system_notify_template`
SET `content` = '您的教师认证审核结果为：{status}。{reason}', `params` = '["status","reason","category","action","bizId","targetType","targetId"]',
    `updater` = 'admin', `update_time` = NOW()
WHERE `code` = 'tutor_certification_audit' AND `deleted` = b'0';
UPDATE `system_notify_template`
SET `content` = '您的{publishType}《{title}》审核结果为：{status}。{reason}', `params` = '["publishType","title","status","reason","category","action","bizId","targetType","targetId"]',
    `updater` = 'admin', `update_time` = NOW()
WHERE `code` = 'tutor_publish_audit' AND `deleted` = b'0';
UPDATE `system_notify_template`
SET `content` = '您有一条家教匹配已双方确认，匹配编号：{matchId}。', `params` = '["matchId"]',
    `updater` = 'admin', `update_time` = NOW()
WHERE `code` = 'tutor_match_success' AND `deleted` = b'0';
UPDATE `system_notify_template`
SET `content` = '用户 {reviewerUserId} 给您提交了 {rating} 星评价。', `params` = '["reviewerUserId","rating"]',
    `updater` = 'admin', `update_time` = NOW()
WHERE `code` = 'tutor_review_created' AND `deleted` = b'0';
UPDATE `system_notify_template`
SET `content` = '您已查看 {counterpartName} 在《{contentTitle}》中的联系方式。是否为复用：{reuse}。请注意核验身份并保护个人信息。',
    `params` = '["category","action","bizId","targetType","targetId","counterpartUserId","counterpartName","contentTitle","reuse"]',
    `updater` = 'admin', `update_time` = NOW()
WHERE `code` = 'tutor_contact_viewer' AND `deleted` = b'0';
UPDATE `system_notify_template`
SET `content` = '{counterpartName} 查看了您在《{contentTitle}》中的联系方式。是否为复用：{reuse}。请留意后续沟通并注意安全。',
    `params` = '["category","action","bizId","targetType","targetId","counterpartUserId","counterpartName","contentTitle","reuse"]',
    `updater` = 'admin', `update_time` = NOW()
WHERE `code` = 'tutor_contact_owner' AND `deleted` = b'0';
