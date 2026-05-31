# Tutor Point Task Loop Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Complete the non-recharge point acquisition loop with daily sign-in, profile initialization, role profile completion, and bilateral five-star review rewards.

**Architecture:** Keep the member module as the point ledger and sign-in owner. Add a tutor reward record table and `TutorPointRewardService` for idempotent tutor-specific rewards, expose a tutor task summary API, and render those tasks in the uni-app score page. Add a dedicated teacher profile page so the teacher completion task has a real destination.

**Tech Stack:** Spring Boot 2.7, Maven, MyBatis Plus, Mockito/JUnit 5, MySQL, uni-app Vue 3, JavaScript, pnpm/npm Prettier.

---

## Repository Boundaries

Backend work is in `D:\work\jiajiao\zfwl`. Mobile work is in `D:\work\jiajiao\zfwl-unapp`. Treat them as independent git repositories and run `git status --short` inside each before editing or committing.

## File Map

Backend files:

- Create `sql/mysql/tutor_point_reward_record.sql`: reward record schema.
- Modify `sql/mysql/jiajiao_mysql_all.sql`: merge the reward table into the all-in-one schema.
- Modify `yudao-module-member/src/main/java/cn/iocoder/yudao/module/member/enums/point/MemberPointBizTypeEnum.java`: add tutor task ledger descriptions.
- Create `yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/dal/dataobject/point/TutorPointRewardRecordDO.java`: reward record entity.
- Create `yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/dal/mysql/point/TutorPointRewardRecordMapper.java`: idempotency and completion queries.
- Create `yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/enums/point/TutorPointTaskTypeEnum.java`: task identifiers, points, and labels.
- Create `yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/service/point/TutorPointRewardService.java`.
- Create `yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/service/point/TutorPointRewardServiceImpl.java`: idempotent ledger integration.
- Modify profile, parent profile, teacher profile, and review services: invoke rewards transactionally.
- Create `yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/controller/app/point/AppTutorPointController.java`.
- Create `yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/controller/app/point/vo/AppTutorPointTaskRespVO.java`.

Mobile files:

- Modify `pages.json`: register teacher profile page.
- Create `pages/tutor/teacher-profile/index.vue`: teacher profile form.
- Create `sheep/api/tutor/point.js`: tutor task API.
- Modify `pages/user/wallet/score.vue`: task card, sign-in, navigation, refresh.

## Task 1: Add Tutor Reward Schema and Enums

**Files:**
- Create: `zfwl/sql/mysql/tutor_point_reward_record.sql`
- Modify: `zfwl/sql/mysql/jiajiao_mysql_all.sql`
- Modify: `zfwl/yudao-module-member/src/main/java/cn/iocoder/yudao/module/member/enums/point/MemberPointBizTypeEnum.java`
- Create: `zfwl/yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/enums/point/TutorPointTaskTypeEnum.java`
- Create: `zfwl/yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/dal/dataobject/point/TutorPointRewardRecordDO.java`
- Create: `zfwl/yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/dal/mysql/point/TutorPointRewardRecordMapper.java`

- [ ] **Step 1: Create the reward table SQL**

Use:

```sql
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
```

- [ ] **Step 2: Add member ledger business types**

Add enum entries with unused tutor ranges:

```java
TUTOR_PROFILE_INIT(101, "家教身份档案奖励", "首次选择家教身份获得 {} 积分", true),
TUTOR_ROLE_PROFILE_COMPLETE(102, "家教资料完善奖励", "首次完善家教资料获得 {} 积分", true),
TUTOR_FIVE_STAR_REVIEW(104, "家教五星好评奖励", "家教五星好评获得 {} 积分", true),
```

- [ ] **Step 3: Add tutor task enum**

```java
PROFILE_INIT("profile_init", 20, "选择家教身份"),
ROLE_PROFILE_COMPLETE("role_profile_complete", 30, "完善对应资料"),
FIVE_STAR_REVIEW("five_star_review", 10, "五星评价");
```

- [ ] **Step 4: Add DO and mapper**

Mapper methods:

```java
default TutorPointRewardRecordDO selectByUniqueKey(Long userId, String taskType, String bizId) {
    return selectOne(TutorPointRewardRecordDO::getUserId, userId,
            TutorPointRewardRecordDO::getTaskType, taskType,
            TutorPointRewardRecordDO::getBizId, bizId);
}

default boolean existsByTaskType(Long userId, String taskType) {
    return selectCount(TutorPointRewardRecordDO::getUserId, userId,
            TutorPointRewardRecordDO::getTaskType, taskType) > 0;
}
```

- [ ] **Step 5: Verify compilation**

Run:

```powershell
cd D:\work\jiajiao\zfwl
mvn -pl yudao-module-tutor -am -DskipTests compile
```

Expected: `BUILD SUCCESS`.

- [ ] **Step 6: Commit backend schema**

```powershell
git add sql/mysql yudao-module-member yudao-module-tutor
git commit -m "feat: add tutor point reward schema"
```

## Task 2: Implement Idempotent Reward Service

**Files:**
- Create: `zfwl/yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/service/point/TutorPointRewardService.java`
- Create: `zfwl/yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/service/point/TutorPointRewardServiceImpl.java`
- Create: `zfwl/yudao-module-tutor/src/test/java/cn/iocoder/yudao/module/tutor/service/point/TutorPointRewardServiceImplTest.java`

- [ ] **Step 1: Write failing tests**

Cover these concrete scenarios:

```java
@Test
void reward_whenFirstClaim_insertsRecordAndAddsPoint() {
    when(rewardRecordMapper.selectByUniqueKey(100L, "profile_init", "profile_init")).thenReturn(null);

    boolean rewarded = rewardService.reward(100L, TutorPointTaskTypeEnum.PROFILE_INIT,
            "profile_init", "首次初始化身份档案");

    assertTrue(rewarded);
    verify(rewardRecordMapper).insert(any(TutorPointRewardRecordDO.class));
    verify(memberPointApi).addPoint(100L, 20, MemberPointBizTypeEnum.TUTOR_PROFILE_INIT.getType(),
            "profile_init");
}

@Test
void reward_whenAlreadyClaimed_doesNotAddPointAgain() {
    when(rewardRecordMapper.selectByUniqueKey(100L, "profile_init", "profile_init"))
            .thenReturn(TutorPointRewardRecordDO.builder().id(1L).build());

    boolean rewarded = rewardService.reward(100L, TutorPointTaskTypeEnum.PROFILE_INIT,
            "profile_init", "首次初始化身份档案");

    assertFalse(rewarded);
    verify(memberPointApi, never()).addPoint(anyLong(), anyInt(), anyInt(), anyString());
}
```

Assert `memberPointApi.addPoint(userId, point, bizType, bizId)` is invoked once for a first claim and never invoked for an existing record.

- [ ] **Step 2: Verify red**

```powershell
mvn -pl yudao-module-tutor "-Dtest=TutorPointRewardServiceImplTest" test
```

Expected: compilation failure because `TutorPointRewardServiceImpl` does not exist.

- [ ] **Step 3: Implement reward service**

Public API:

```java
boolean reward(Long userId, TutorPointTaskTypeEnum taskType, String bizId, String remark);
boolean hasReward(Long userId, TutorPointTaskTypeEnum taskType);
```

Implementation rules:

1. Query `selectByUniqueKey`.
2. Return `false` if a record already exists.
3. Insert reward record.
4. Call `MemberPointApi.addPoint`.
5. Return `true`.
6. Catch duplicate-key insert races and return `false`.

Map task type to member ledger type explicitly:

```java
PROFILE_INIT -> TUTOR_PROFILE_INIT
ROLE_PROFILE_COMPLETE -> TUTOR_ROLE_PROFILE_COMPLETE
FIVE_STAR_REVIEW -> TUTOR_FIVE_STAR_REVIEW
```

- [ ] **Step 4: Verify green**

Run the same Maven test command. Expected: `Tests run: 2, Failures: 0`.

- [ ] **Step 5: Commit**

```powershell
git add yudao-module-tutor
git commit -m "feat: add idempotent tutor point rewards"
```

## Task 3: Reward Profile Initialization and Role Profile Completion

**Files:**
- Modify: `zfwl/yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/service/profile/TutorUserProfileServiceImpl.java`
- Modify: `zfwl/yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/service/parent/TutorParentProfileServiceImpl.java`
- Modify: `zfwl/yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/service/teacher/TutorTeacherProfileServiceImpl.java`
- Modify tests for the same services.

- [ ] **Step 1: Add failing tests**

Assert:

- `initProfile` calls reward service with `PROFILE_INIT`, `profile_init`.
- First parent profile insert calls reward service with `ROLE_PROFILE_COMPLETE`, `role_profile_complete`.
- Parent update does not call reward service.
- First teacher profile save calls reward service.
- Teacher update does not call reward service.

- [ ] **Step 2: Verify red**

```powershell
mvn -pl yudao-module-tutor "-Dtest=TutorUserProfileServiceImplTest,TutorParentProfileServiceImplTest,TutorTeacherProfileServiceImplTest" test
```

Expected: failures showing missing reward invocations.

- [ ] **Step 3: Inject and call reward service**

After successful first inserts:

```java
pointRewardService.reward(userId, TutorPointTaskTypeEnum.PROFILE_INIT,
        "profile_init", "首次初始化身份档案");
```

```java
pointRewardService.reward(userId, TutorPointTaskTypeEnum.ROLE_PROFILE_COMPLETE,
        "role_profile_complete", "首次完善资料");
```

Add `@Transactional(rollbackFor = Exception.class)` to parent and teacher save methods so business writes roll back when ledger writes fail.

- [ ] **Step 4: Verify green**

Run the targeted tests. Expected: all pass.

- [ ] **Step 5: Commit**

```powershell
git add yudao-module-tutor
git commit -m "feat: reward tutor profile completion"
```

## Task 4: Make Five-Star Review Reward Bilateral

**Files:**
- Modify: `zfwl/yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/service/review/TutorReviewServiceImpl.java`
- Create or modify: `zfwl/yudao-module-tutor/src/test/java/cn/iocoder/yudao/module/tutor/service/review/TutorReviewServiceImplTest.java`

- [ ] **Step 1: Write failing tests**

Cover:

```java
createReview_whenFiveStars_rewardsReviewerAndTarget()
createReview_whenFourStars_doesNotRewardEitherSide()
```

Expect:

```java
reward(reviewerUserId, FIVE_STAR_REVIEW, matchId + ":reviewer", "五星评价奖励");
reward(targetUserId, FIVE_STAR_REVIEW, matchId + ":target", "五星好评奖励");
```

- [ ] **Step 2: Verify red**

```powershell
mvn -pl yudao-module-tutor "-Dtest=TutorReviewServiceImplTest" test
```

- [ ] **Step 3: Replace direct member ledger writes**

Remove direct `MemberPointApi` usage from review service and route both rewards through `TutorPointRewardService`.

- [ ] **Step 4: Verify green**

Run the targeted review test. Expected: all pass.

- [ ] **Step 5: Commit**

```powershell
git add yudao-module-tutor
git commit -m "feat: reward both sides for five star reviews"
```

## Task 5: Expose Tutor Point Task Summary API

**Files:**
- Create: `zfwl/yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/controller/app/point/AppTutorPointController.java`
- Create: `zfwl/yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/controller/app/point/vo/AppTutorPointTaskRespVO.java`
- Create: `zfwl/yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/service/point/TutorPointTaskService.java`
- Create: `zfwl/yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/service/point/TutorPointTaskServiceImpl.java`
- Create: `zfwl/yudao-module-tutor/src/test/java/cn/iocoder/yudao/module/tutor/service/point/TutorPointTaskServiceImplTest.java`
- Use existing: `zfwl/yudao-module-member/src/main/java/cn/iocoder/yudao/module/member/service/signin/MemberSignInRecordService.java`

- [ ] **Step 1: Write failing aggregation tests**

Cover parent, teacher, and no-profile users. Assert:

- Daily sign-in task uses today's sign-in status.
- Profile initialization completion follows `TutorUserProfileDO`.
- Role profile completion checks the matching parent or teacher mapper.
- Parent path is `/pages/tutor/parent-profile/index`.
- Teacher path is `/pages/tutor/teacher-profile/index`.
- Missing profile path is `/pages/tutor/identity/index`.
- Five-star review action path is `/pages/tutor/reviews/index`.

- [ ] **Step 2: Verify red**

```powershell
mvn -pl yudao-module-tutor "-Dtest=TutorPointTaskServiceImplTest" test
```

- [ ] **Step 3: Implement task service and controller**

Inject `MemberSignInRecordService` and calculate the daily state explicitly:

```java
boolean todaySignedIn = Boolean.TRUE.equals(
        signInRecordService.getSignInRecordSummary(userId).getTodaySignIn());
```

Controller:

```java
@GetMapping("/tasks")
public CommonResult<List<AppTutorPointTaskRespVO>> getTaskList() {
    return success(pointTaskService.getTaskList(getLoginUserId()));
}
```

Use `GET /tutor/points/tasks`.

- [ ] **Step 4: Verify green and module regression**

```powershell
mvn -pl yudao-module-tutor test
```

Expected: new tests pass. If the existing unrelated square distance rounding test still fails with expected `1.7` and actual `1.6`, report it separately and do not change it in this feature.

- [ ] **Step 5: Commit**

```powershell
git add yudao-module-tutor yudao-module-member
git commit -m "feat: expose tutor point tasks"
```

## Task 6: Add Mobile Teacher Profile Page

**Files:**
- Modify: `zfwl-unapp/pages.json`
- Create: `zfwl-unapp/pages/tutor/teacher-profile/index.vue`
- Use existing: `zfwl-unapp/sheep/api/tutor/teacher-profile.js`
- Create: `zfwl-unapp/tests/tutor-teacher-profile-page.test.cjs`

- [ ] **Step 1: Write failing static regression test**

Assert:

- `pages.json` registers `tutor/teacher-profile/index`.
- Page imports `TutorTeacherProfileApi`.
- Page calls `getProfile()` and `saveProfile`.
- Form contains required teacher fields.

- [ ] **Step 2: Verify red**

```powershell
cd D:\work\jiajiao\zfwl-unapp
node tests\tutor-teacher-profile-page.test.cjs
```

- [ ] **Step 3: Build teacher profile page**

Follow the visual structure of `pages/tutor/parent-profile/index.vue`. Include inputs and switches for every field accepted by `AppTutorTeacherProfileSaveReqVO`. Validate required fields before submit.

- [ ] **Step 4: Verify**

```powershell
node tests\tutor-teacher-profile-page.test.cjs
.\node_modules\.bin\prettier.cmd --check pages.json pages\tutor\teacher-profile\index.vue tests\tutor-teacher-profile-page.test.cjs
```

- [ ] **Step 5: Commit mobile teacher page**

```powershell
git add pages.json pages/tutor/teacher-profile tests
git commit -m "feat: add mobile teacher profile page"
```

## Task 7: Add Mobile Point Task Card and Sign-In Loop

**Files:**
- Create: `zfwl-unapp/sheep/api/tutor/point.js`
- Modify: `zfwl-unapp/pages/user/wallet/score.vue`
- Create: `zfwl-unapp/tests/tutor-point-task-loop.test.cjs`

- [ ] **Step 1: Write failing static regression test**

Assert:

- Score page imports `SignInApi` and tutor point API.
- Score page renders “做任务赚积分”.
- Score page loads `/tutor/points/tasks`.
- `sign_in` action calls `SignInApi.createSignInRecord()`.
- Successful sign-in refreshes user info, tasks, and point records.
- Navigate action uses returned `task.path`.

- [ ] **Step 2: Verify red**

```powershell
node tests\tutor-point-task-loop.test.cjs
```

- [ ] **Step 3: Add API and UI**

API:

```js
getTaskList: () =>
  request({
    url: '/tutor/points/tasks',
    method: 'GET',
    custom: { auth: true, showLoading: false },
  }),
```

Render each task with reward copy, completion state, and action button. Keep five-star task navigable even after prior rewards.

- [ ] **Step 4: Verify mobile files**

```powershell
node tests\tutor-point-task-loop.test.cjs
node tests\tutor-teacher-profile-page.test.cjs
.\node_modules\.bin\prettier.cmd --check pages.json pages\user\wallet\score.vue pages\tutor\teacher-profile\index.vue sheep\api\tutor\point.js tests\*.test.cjs
git diff --check
```

- [ ] **Step 5: Commit**

```powershell
git add pages sheep tests
git commit -m "feat: add mobile point task loop"
```

## Task 8: End-to-End Verification

- [ ] **Step 1: Verify backend targeted tests**

```powershell
cd D:\work\jiajiao\zfwl
mvn -pl yudao-module-tutor "-Dtest=TutorPointRewardServiceImplTest,TutorUserProfileServiceImplTest,TutorParentProfileServiceImplTest,TutorTeacherProfileServiceImplTest,TutorReviewServiceImplTest,TutorPointTaskServiceImplTest" test
```

- [ ] **Step 2: Verify backend diff**

```powershell
git diff --check
git status --short
```

- [ ] **Step 3: Verify mobile static regressions**

```powershell
cd D:\work\jiajiao\zfwl-unapp
node tests\tutor-point-task-loop.test.cjs
node tests\tutor-teacher-profile-page.test.cjs
node tests\tutor-detail-action-bar.test.cjs
node tests\tutor-city-selection.test.cjs
.\node_modules\.bin\prettier.cmd --check pages.json pages sheep tests
git diff --check
git status --short
```

- [ ] **Step 4: Manual smoke checklist**

1. New user selects identity and sees `+20` in point records.
2. Parent first saves parent profile and sees `+30`; second save adds nothing.
3. Teacher first saves teacher profile and sees `+30`; second save adds nothing.
4. User signs in from score page and sees configured sign-in reward and completed state.
5. A confirmed match receives a five-star review and both users receive `+10`.
6. Four-star review produces no point reward.
7. Viewing contact still deducts `10`, with free re-view within 30 days.
