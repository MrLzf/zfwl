# Tutor Message Center Closed Loop Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Deliver audit, contact, and point notification loops with categorized unread state and mobile navigation.

**Architecture:** Reuse `system_notify_message`. The tutor module enriches notification template parameters, emits notifications from completed business actions, and exposes member-facing categorized message APIs. The uni-app frontend renders category summaries and a paginated category list.

**Tech Stack:** Spring Boot 2.7, MyBatis Plus, JUnit 5, Mockito, Uni-app Vue 3, Node source regression tests

---

### Task 1: Enrich tutor notification metadata and SQL templates

**Files:**
- Modify: `yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/service/notify/TutorNotifyService.java`
- Modify: `yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/service/notify/TutorNotifyServiceImpl.java`
- Modify: `sql/mysql/tutor_notify.sql`
- Modify: `sql/mysql/jiajiao_mysql_all.sql`
- Test: `yudao-module-tutor/src/test/java/cn/iocoder/yudao/module/tutor/service/notify/TutorNotifyServiceImplTest.java`

- [ ] Add failing tests that capture outgoing `NotifySendSingleToUserReqDTO` values for audit, point, viewer-contact and owner-contact notifications.
- [ ] Run `mvn -pl yudao-module-tutor -am -Dtest=TutorNotifyServiceImplTest -Dsurefire.failIfNoSpecifiedTests=false test` and confirm failure because contact methods and common metadata do not exist.
- [ ] Add common `category`, `action`, `bizId`, `targetType`, `targetId` parameters. Add `sendContactViewedToViewer(...)` and `sendContactViewedToOwner(...)`. Extend point notifications with `totalPoint` and title. Keep notification send failures non-blocking.
- [ ] Update SQL templates idempotently: update existing tutor template definitions and insert two contact template codes, `tutor_contact_viewer` and `tutor_contact_owner`.
- [ ] Re-run the focused test and commit backend notification metadata.

### Task 2: Emit contact and point notifications from completed actions

**Files:**
- Modify: `yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/service/contact/TutorContactServiceImpl.java`
- Modify: `yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/service/point/TutorPointRewardServiceImpl.java`
- Modify: `yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/controller/admin/point/AdminTutorPointController.java`
- Test: `yudao-module-tutor/src/test/java/cn/iocoder/yudao/module/tutor/service/contact/TutorContactServiceImplTest.java`
- Test: `yudao-module-tutor/src/test/java/cn/iocoder/yudao/module/tutor/service/point/TutorPointRewardServiceImplTest.java`

- [ ] Add failing tests asserting first unlock and free reuse both send viewer and owner contact notifications, and point rewards send point notifications after successful member point writes.
- [ ] Run focused tutor tests and confirm the missing sends fail.
- [ ] Send contact notifications after successful target lookup and contact resolution. For first unlock include `reuse=false`; for free reuse include `reuse=true`. Resolve display names through `MemberUserApi` without exposing contact values.
- [ ] Send point notifications for tutor reward and admin adjustment paths after successful point mutation, including changed value and resulting balance when available.
- [ ] Re-run focused tests and commit business triggers.

### Task 3: Add categorized member-facing message APIs

**Files:**
- Create: `yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/controller/app/message/AppTutorMessageController.java`
- Create: `yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/controller/app/message/vo/AppTutorMessagePageReqVO.java`
- Create: `yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/controller/app/message/vo/AppTutorMessageRespVO.java`
- Create: `yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/controller/app/message/vo/AppTutorMessageSummaryRespVO.java`
- Create: `yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/service/message/TutorMessageService.java`
- Create: `yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/service/message/TutorMessageServiceImpl.java`
- Modify: `yudao-module-system/src/main/java/cn/iocoder/yudao/module/system/service/notify/NotifyMessageService.java`
- Modify: `yudao-module-system/src/main/java/cn/iocoder/yudao/module/system/service/notify/NotifyMessageServiceImpl.java`
- Modify: `yudao-module-system/src/main/java/cn/iocoder/yudao/module/system/dal/mysql/notify/NotifyMessageMapper.java`
- Test: `yudao-module-tutor/src/test/java/cn/iocoder/yudao/module/tutor/service/message/TutorMessageServiceImplTest.java`

- [ ] Add failing service tests for three-category summary, category pagination, owned-message read, category read-all, and all-tutor-message read-all.
- [ ] Run focused message tests and confirm failure because the service does not exist.
- [ ] Add system notification query helpers scoped by member user and tutor template codes. Filter categories from `templateParams.category`, map template parameters to stable App response fields, and enforce ownership on read updates.
- [ ] Expose `GET /tutor/messages/summary`, `GET /tutor/messages/page`, `PUT /tutor/messages/read`, and `PUT /tutor/messages/read-all`.
- [ ] Run `mvn -pl yudao-module-tutor -am test` and commit App message APIs.

### Task 4: Build mobile message summaries and category list

**Files:**
- Create: `sheep/api/tutor/message.js`
- Modify: `pages/index/message.vue`
- Create: `pages/tutor/messages/index.vue`
- Modify: `pages.json`
- Test: `tests/tutor-message-center-loop.test.cjs`

- [ ] Add a failing Node regression test asserting message API endpoints, summary loading, unread badges, list route registration, read-all actions, and the four action path mappings.
- [ ] Run `node --test tests/tutor-message-center-loop.test.cjs` and confirm failure because the mobile API and list page do not exist.
- [ ] Add the API wrapper for summary, category page, single read and read-all.
- [ ] Replace static message cards with API summaries, latest time, unread badges, retry handling and global read-all.
- [ ] Add the category list with pagination, unread styles, category read-all, non-blocking single-read and action navigation.
- [ ] Register `pages/tutor/messages/index` in `pages.json`.
- [ ] Run `node --test tests/*.test.cjs` and `npx prettier --check pages/index/message.vue pages/tutor/messages/index.vue sheep/api/tutor/message.js tests/tutor-message-center-loop.test.cjs`.

### Task 5: Verify the closed loop

**Files:**
- Modify: `qa/tutor-p0-qa.ps1`

- [ ] Extend QA requests to verify message summary, category pagination, single read and read-all after certification audit and contact unlock.
- [ ] Run `mvn -pl yudao-module-tutor -am test`.
- [ ] Run `node --test tests/*.test.cjs` in `zfwl-unapp`.
- [ ] Run `git diff --check` in both repositories.
- [ ] Commit backend QA coverage and mobile implementation separately.
