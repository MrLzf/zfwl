# 家教积分任务闭环设计

## 目标

在不实现在线充值的前提下，补齐家教业务中的积分获取闭环。用户可以在移动端积分页查看任务、完成任务、领取积分并查看积分明细。

## 积分规则

| 任务 | 奖励 | 发放条件 | 重复限制 |
| --- | ---: | --- | --- |
| 每日签到 | 按会员签到后台配置 | 用户当天首次签到成功 | 沿用会员签到模块规则 |
| 初始化身份档案 | 20 积分 | 用户首次成功选择家长或教师身份 | 每个用户仅奖励一次 |
| 完善对应资料 | 30 积分 | 家长首次成功保存家长资料，或教师首次成功保存教师资料 | 每个用户仅奖励一次 |
| 五星评价 | 双方各 10 积分 | 用户针对已匹配记录首次提交五星评价 | 同一评价双方各奖励一次 |

查看联系方式继续消耗 10 积分，同一联系人 30 天内复看免费。在线充值不在本期范围内，后台人工调整积分保留现状。

## 架构

会员模块继续作为积分账本。签到继续使用会员模块已有签到接口和后台签到配置。

家教模块新增任务奖励服务 `TutorPointRewardService`，负责身份档案、资料完善和五星评价三类家教奖励。业务服务在事务内调用奖励服务，奖励服务通过独立记录表保证幂等，再调用 `MemberPointApi.addPoint` 写入会员积分账本。

移动端积分页新增任务区。家教模块提供任务列表 API，返回任务展示信息和完成状态。前端仅负责渲染、调用签到接口和跳转，不自行判断奖励是否到账。

## 数据模型

新增表 `tutor_point_reward_record`：

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `id` | bigint | 主键 |
| `user_id` | bigint | 会员用户编号 |
| `task_type` | varchar(32) | `profile_init`、`role_profile_complete`、`five_star_review` |
| `biz_id` | varchar(64) | 幂等业务编号 |
| `point` | int | 发放积分 |
| `remark` | varchar(255) | 发放说明 |
| 通用字段 | - | 创建人、创建时间、更新人、更新时间、删除标记、租户编号 |

唯一键：`user_id + task_type + biz_id + deleted`。

固定任务的 `biz_id`：

- 身份档案：`profile_init`
- 对应资料：`role_profile_complete`
- 五星评价提交人：`匹配记录编号:reviewer`
- 五星评价被评价人：`匹配记录编号:target`

## 后端流程

### 身份档案奖励

`TutorUserProfileServiceImpl.initProfile` 成功插入身份档案后，在同一事务内调用奖励服务：

```text
reward(userId, profile_init, profile_init, 20, "首次初始化身份档案")
```

### 对应资料奖励

`TutorParentProfileServiceImpl.saveParentProfile` 和 `TutorTeacherProfileServiceImpl.saveTeacherProfile` 在首次插入资料后调用奖励服务：

```text
reward(userId, role_profile_complete, role_profile_complete, 30, "首次完善资料")
```

后续修改不触发奖励。

### 五星评价奖励

`TutorReviewServiceImpl.createReview` 在用户提交五星评价时，通过统一奖励服务给评价人和被评价人各发放 10 积分：

```text
reward(reviewerUserId, five_star_review, matchId + ":reviewer", 10, "五星评价奖励")
reward(targetUserId, five_star_review, matchId + ":target", 10, "五星好评奖励")
```

非五星评价不奖励。同一用户针对同一匹配只能评价一次，奖励记录唯一键再提供一层幂等保护。

### 幂等与事务

奖励服务先插入奖励记录，再调用 `MemberPointApi.addPoint`。唯一键冲突表示奖励已发放，直接按成功返回，不重复加分。

奖励触发与业务保存使用同一事务。积分发放失败时，业务保存回滚，避免出现任务已完成但积分未到账的状态。

## 任务列表 API

新增 App API：

```text
GET /app-api/tutor/points/tasks
```

返回结构：

```json
[
  {
    "type": "daily_sign_in",
    "title": "每日签到",
    "description": "每天签到领取积分",
    "point": null,
    "completed": false,
    "action": "sign_in",
    "path": ""
  },
  {
    "type": "profile_init",
    "title": "选择家教身份",
    "description": "首次选择家长或教师身份",
    "point": 20,
    "completed": true,
    "action": "navigate",
    "path": "/pages/tutor/identity/index"
  }
]
```

任务列表固定包含：

- `daily_sign_in`
- `profile_init`
- `role_profile_complete`
- `five_star_review`

对应资料跳转根据用户身份返回：

- 家长：`/pages/tutor/parent-profile/index`
- 教师：`/pages/tutor/teacher-profile/index`
- 未选择身份：`/pages/tutor/identity/index`

五星评价跳转：`/pages/tutor/reviews/index`。

签到任务的 `point` 允许为空，因为实际奖励由后台签到配置决定。`completed` 表示用户当天是否已签到。

## 移动端页面

修改 `pages/user/wallet/score.vue`：

- 保留余额、积分说明、积分不足提示和明细列表。
- 增加“做任务赚积分”卡片。
- 展示任务名称、说明、奖励积分、完成状态和按钮。
- 签到按钮直接调用现有 `SignInApi.createSignInRecord()`。
- 签到成功后刷新用户信息、任务列表和积分明细。
- 导航任务使用 API 返回路径跳转。
- 已完成的一次性任务显示“已完成”。
- 五星评价任务始终保留“去评价”入口，因为用户后续仍可能产生新的匹配。

新增 `pages/tutor/teacher-profile/index.vue`：

- 调用现有 `TutorTeacherProfileApi.getProfile()` 和 `TutorTeacherProfileApi.saveProfile`。
- 提供学历、学校、专业、教师资格证状态、可授科目、授课模式、时薪范围、服务半径、试课设置、教龄和教学介绍表单。
- 首次成功保存后由后端发放资料完善奖励。

## SQL

新增独立 SQL 文件，并同步合并到全量脚本：

- `sql/mysql/tutor_point_reward_record.sql`
- `sql/mysql/jiajiao_mysql_all.sql`

## 异常处理

- 奖励记录重复插入：视为已领取，不返回错误。
- 会员积分发放失败：事务回滚，业务保存失败。
- 重复签到：沿用会员签到模块提示。
- 任务列表查询失败：返回统一接口错误，前端提示重试并保留当前页面已有内容。
- 未选择身份时点击资料完善：跳转身份选择页。

## 测试

后端单元测试覆盖：

- 首次初始化身份档案发放 20 积分。
- 重复身份档案奖励不重复发放。
- 家长首次保存资料发放 30 积分，修改不重复发放。
- 教师首次保存资料发放 30 积分，修改不重复发放。
- 首次五星评价给评价人和被评价人各发放 10 积分。
- 非五星评价不发放积分。
- 同一评价的双方五星奖励不重复发放。
- 任务列表正确聚合签到、身份档案和资料完善状态。
- 任务列表按家长、教师、未选择身份返回正确跳转路径。

移动端静态回归检查覆盖：

- 积分页展示任务区。
- 签到操作调用现有签到 API。
- 导航任务使用后端返回路径。
- 签到成功后刷新用户信息、任务列表和积分明细。
- 教师资料任务跳转到新增教师资料页。

## 非目标

- 在线充值。
- 复杂积分商城。
- 连续签到之外的新签到规则。
- 邀请好友奖励。
- 管理后台新增任务配置页面。
