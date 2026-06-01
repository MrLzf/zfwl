# Tutor Point WeChat Miniapp Recharge Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add configurable point packages and a WeChat mini-program payment flow that credits the shared member point balance exactly once after a verified payment.

**Architecture:** Keep the member module as the point ledger and add package and recharge-order ownership to `yudao-module-tutor`. Reuse `yudao-module-pay` for `wx_lite` payment submission, WeChat callback verification, and notify retries. Add focused Vue admin and uni-app surfaces without storing merchant secrets in Git.

**Tech Stack:** Spring Boot 2.7, Java 8, MyBatis Plus, JUnit 5, Mockito, Vue 3, TypeScript, Element Plus, uni-app, Node regression tests, MySQL.

---

## File Structure

Backend additions live under `yudao-module-tutor`:

- `dal/dataobject/pointrecharge/`: package and order persistence models.
- `dal/mysql/pointrecharge/`: package and order mappers, including conditional paid update.
- `service/pointrecharge/`: package CRUD, order creation, payment submission, verified callback, and virtual-item shipping upload.
- `controller/app/pointrecharge/`: member package listing, order creation, payment, and status APIs.
- `controller/admin/pointrecharge/`: package administration and internal payment notification endpoint.

Frontend additions:

- `zlwl-vue/src/api/tutor/pointRecharge/` and `src/views/tutor/pointRecharge/`: admin package management.
- `zfwl-unapp/sheep/api/tutor/point-recharge.js`: mobile recharge API.
- `zfwl-unapp/pages/user/wallet/score.vue`: package selection and `uni.requestPayment`.

Configuration:

- Use the existing pay application and channel administration UI for merchant configuration.
- Add a tutor point recharge pay app key property with a non-secret default.
- Never commit merchant keys, private keys, certificates, or extracted certificate files.

## Task 1: Add Point Recharge Schema And Module Wiring

**Files:**
- Create: `sql/mysql/tutor_point_recharge.sql`
- Modify: `pom.xml`
- Modify: `yudao-module-tutor/pom.xml`
- Modify: `yudao-module-member/src/main/java/cn/iocoder/yudao/module/member/enums/point/MemberPointBizTypeEnum.java`
- Modify: `yudao-module-pay/src/main/java/cn/iocoder/yudao/module/pay/framework/pay/config/PayProperties.java`
- Modify: `yudao-server/src/main/resources/application-local.yaml`
- Modify: `yudao-server/src/main/resources/application-dev.yaml`

- [ ] **Step 1: Verify current module wiring**

Run:

```powershell
mvn -pl yudao-module-tutor -am test -DskipTests
```

Expected: compilation succeeds before adding the pay-module dependency.

- [ ] **Step 2: Add the schema**

Create `sql/mysql/tutor_point_recharge.sql` with:

```sql
CREATE TABLE IF NOT EXISTS `tutor_point_recharge_package` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '套餐编号',
  `name` varchar(64) NOT NULL COMMENT '套餐名称',
  `pay_price` int NOT NULL COMMENT '支付金额，单位分',
  `point` int NOT NULL COMMENT '基础积分',
  `bonus_point` int NOT NULL DEFAULT 0 COMMENT '赠送积分',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
  `creator` varchar(64) DEFAULT '',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updater` varchar(64) DEFAULT '',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` bit NOT NULL DEFAULT b'0',
  `tenant_id` bigint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='家教积分购买套餐';

CREATE TABLE IF NOT EXISTS `tutor_point_recharge_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单编号',
  `user_id` bigint NOT NULL COMMENT '会员编号',
  `package_id` bigint NOT NULL COMMENT '套餐编号',
  `pay_price` int NOT NULL COMMENT '支付金额，单位分',
  `point` int NOT NULL COMMENT '基础积分',
  `bonus_point` int NOT NULL DEFAULT 0 COMMENT '赠送积分',
  `pay_order_id` bigint DEFAULT NULL COMMENT '支付单编号',
  `pay_status` bit NOT NULL DEFAULT b'0' COMMENT '是否已支付',
  `pay_channel_code` varchar(32) DEFAULT NULL COMMENT '支付渠道',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `creator` varchar(64) DEFAULT '',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updater` varchar(64) DEFAULT '',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` bit NOT NULL DEFAULT b'0',
  `tenant_id` bigint NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_pay_order_id` (`pay_order_id`)
) ENGINE=InnoDB COMMENT='家教积分购买订单';
```

- [ ] **Step 3: Wire modules and non-secret property**

Add `yudao-module-pay` to the root modules if it is not already enabled. Add a `yudao-module-pay` dependency to `yudao-module-tutor/pom.xml`. Add:

```java
TUTOR_POINT_RECHARGE(202, "家教积分购买", "购买获得 {} 积分", true),
```

to `MemberPointBizTypeEnum`, and add:

```java
private String tutorPointRechargePayAppKey = "tutor-point-recharge";
```

to `PayProperties`. Add only the non-secret property override to YAML:

```yaml
yudao:
  pay:
    tutor-point-recharge-pay-app-key: tutor-point-recharge
```

- [ ] **Step 4: Compile and commit**

Run:

```powershell
mvn -pl yudao-module-tutor -am test -DskipTests
```

Expected: BUILD SUCCESS.

Commit:

```powershell
git add pom.xml yudao-module-tutor/pom.xml yudao-module-member/src/main/java/cn/iocoder/yudao/module/member/enums/point/MemberPointBizTypeEnum.java yudao-module-pay/src/main/java/cn/iocoder/yudao/module/pay/framework/pay/config/PayProperties.java yudao-server/src/main/resources/application-local.yaml yudao-server/src/main/resources/application-dev.yaml sql/mysql/tutor_point_recharge.sql
git commit -m "feat(tutor): add point recharge schema"
```

## Task 2: Add Recharge Package Persistence And Service

**Files:**
- Create: `yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/dal/dataobject/pointrecharge/TutorPointRechargePackageDO.java`
- Create: `yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/dal/mysql/pointrecharge/TutorPointRechargePackageMapper.java`
- Create: `yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/service/pointrecharge/TutorPointRechargePackageService.java`
- Create: `yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/service/pointrecharge/TutorPointRechargePackageServiceImpl.java`
- Test: `yudao-module-tutor/src/test/java/cn/iocoder/yudao/module/tutor/service/pointrecharge/TutorPointRechargePackageServiceImplTest.java`

- [ ] **Step 1: Write failing service tests**

Cover these named tests:

```java
@Test void getEnabledPackages_returnsOnlyEnabledSortedPackages()
@Test void validPackage_rejectsMissingPackage()
@Test void validPackage_rejectsDisabledPackage()
@Test void createPackage_rejectsNonPositivePriceOrPoint()
```

Use Mockito mapper stubs and assert the service returns sorted active packages or throws a service exception.

- [ ] **Step 2: Run tests and observe RED**

Run:

```powershell
mvn -pl yudao-module-tutor -am -Dtest=TutorPointRechargePackageServiceImplTest -Dsurefire.failIfNoSpecifiedTests=false test
```

Expected: FAIL because the recharge package classes do not exist.

- [ ] **Step 3: Implement minimal package service**

Define:

```java
List<TutorPointRechargePackageDO> getEnabledPackageList();
TutorPointRechargePackageDO validEnabledPackage(Long id);
Long createPackage(AdminTutorPointRechargePackageSaveReqVO reqVO);
void updatePackage(AdminTutorPointRechargePackageSaveReqVO reqVO);
void updatePackageStatus(Long id, Integer status);
void deletePackage(Long id);
PageResult<TutorPointRechargePackageDO> getPackagePage(PageParam pageParam);
```

Use `CommonStatusEnum.ENABLE` for the mobile list and reject non-positive `payPrice`, non-positive `point`, or negative `bonusPoint`.

- [ ] **Step 4: Run tests and commit**

Run the Task 2 test command. Expected: PASS.

Commit:

```powershell
git add yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/dal yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/service/pointrecharge yudao-module-tutor/src/test/java/cn/iocoder/yudao/module/tutor/service/pointrecharge
git commit -m "feat(tutor): add point recharge packages"
```

## Task 3: Add Verified Recharge Order And Idempotent Credit

**Files:**
- Create: `yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/dal/dataobject/pointrecharge/TutorPointRechargeOrderDO.java`
- Create: `yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/dal/mysql/pointrecharge/TutorPointRechargeOrderMapper.java`
- Create: `yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/service/pointrecharge/TutorPointRechargeOrderService.java`
- Create: `yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/service/pointrecharge/TutorPointRechargeOrderServiceImpl.java`
- Test: `yudao-module-tutor/src/test/java/cn/iocoder/yudao/module/tutor/service/pointrecharge/TutorPointRechargeOrderServiceImplTest.java`

- [ ] **Step 1: Write failing tests for order creation and callback**

Cover:

```java
@Test void createOrder_copiesPackageValuesAndCreatesPayOrder()
@Test void createOrder_rejectsDisabledPackage()
@Test void updatePaid_creditsBaseAndBonusPointOnce()
@Test void updatePaid_returnsForDuplicateMatchingNotify()
@Test void updatePaid_rejectsDifferentPayOrder()
@Test void updatePaid_rejectsPriceMismatch()
@Test void updatePaid_rejectsMerchantOrderMismatch()
@Test void updatePaid_rejectsNonSuccessPayOrder()
@Test void updatePaid_doesNotCreditWhenConditionalUpdateLosesRace()
```

The happy-path assertion must verify:

```java
verify(memberPointApi).addPoint(userId, point + bonusPoint,
        MemberPointBizTypeEnum.TUTOR_POINT_RECHARGE.getType(), orderId.toString());
```

- [ ] **Step 2: Run tests and observe RED**

Run:

```powershell
mvn -pl yudao-module-tutor -am -Dtest=TutorPointRechargeOrderServiceImplTest -Dsurefire.failIfNoSpecifiedTests=false test
```

Expected: FAIL because recharge order classes do not exist.

- [ ] **Step 3: Implement minimal order creation**

Create the business order from `validEnabledPackage(packageId)`, then call:

```java
payOrderApi.createOrder(new PayOrderCreateReqDTO()
        .setAppKey(payProperties.getTutorPointRechargePayAppKey())
        .setUserIp(userIp)
        .setUserId(userId)
        .setUserType(UserTypeEnum.MEMBER.getValue())
        .setMerchantOrderId(order.getId().toString())
        .setSubject("家教积分购买")
        .setBody(packageDO.getName())
        .setPrice(order.getPayPrice())
        .setExpireTime(addTime(Duration.ofHours(2))));
```

- [ ] **Step 4: Implement conditional paid update**

Add mapper method:

```java
default int updatePaid(Long id, Long payOrderId, String channelCode, LocalDateTime payTime) {
    return update(new TutorPointRechargeOrderDO()
                    .setPayStatus(true).setPayChannelCode(channelCode).setPayTime(payTime),
            new LambdaUpdateWrapper<TutorPointRechargeOrderDO>()
                    .eq(TutorPointRechargeOrderDO::getId, id)
                    .eq(TutorPointRechargeOrderDO::getPayOrderId, payOrderId)
                    .eq(TutorPointRechargeOrderDO::getPayStatus, false));
}
```

After validating the pay order, credit only when this update returns `1`. Treat matching already-paid orders as successful duplicate notifications.

- [ ] **Step 5: Run tests and commit**

Run Task 3 tests. Expected: PASS.

Commit:

```powershell
git add yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/dal yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/service/pointrecharge yudao-module-tutor/src/test/java/cn/iocoder/yudao/module/tutor/service/pointrecharge
git commit -m "feat(tutor): add idempotent point recharge orders"
```

## Task 4: Expose App APIs And Internal Notify Endpoint

**Files:**
- Create: `yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/controller/app/pointrecharge/AppTutorPointRechargeController.java`
- Create: `yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/controller/app/pointrecharge/vo/AppTutorPointRechargePackageRespVO.java`
- Create: `yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/controller/app/pointrecharge/vo/AppTutorPointRechargeOrderCreateReqVO.java`
- Create: `yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/controller/app/pointrecharge/vo/AppTutorPointRechargeOrderRespVO.java`
- Create: `yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/controller/admin/pointrecharge/AdminTutorPointRechargeNotifyController.java`
- Test: `yudao-module-tutor/src/test/java/cn/iocoder/yudao/module/tutor/controller/app/pointrecharge/AppTutorPointRechargeControllerTest.java`

- [ ] **Step 1: Write failing controller tests**

Use MockMvc to assert:

```java
GET  /app-api/tutor/point-recharge/packages
POST /app-api/tutor/point-recharge/orders
GET  /app-api/tutor/point-recharge/orders/{id}
POST /admin-api/tutor/point-recharge/update-paid
```

Assert order creation accepts only:

```json
{"packageId": 1}
```

Do not add a tutor-specific payment-submit endpoint. The mobile client submits the returned `payOrderId` through the existing `/app-api/pay/order/submit` endpoint with `channelCode = wx_lite`.

- [ ] **Step 2: Run tests and observe RED**

Run:

```powershell
mvn -pl yudao-module-tutor -am -Dtest=AppTutorPointRechargeControllerTest -Dsurefire.failIfNoSpecifiedTests=false test
```

Expected: FAIL because controllers do not exist.

- [ ] **Step 3: Implement controllers**

Use `getLoginUserId()` and `getClientIP()` for member endpoints. Use `@PermitAll` only on the internal notify endpoint and delegate all verification to the order service.

- [ ] **Step 4: Run tests and commit**

Run Task 4 tests. Expected: PASS.

Commit:

```powershell
git add yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/controller yudao-module-tutor/src/test/java/cn/iocoder/yudao/module/tutor/controller
git commit -m "feat(tutor): expose point recharge APIs"
```

## Task 5: Add Admin Package API, Menu SQL, And Vue Page

**Files:**
- Create: `yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/controller/admin/pointrecharge/AdminTutorPointRechargePackageController.java`
- Create: `yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/controller/admin/pointrecharge/vo/AdminTutorPointRechargePackageSaveReqVO.java`
- Create: `yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/controller/admin/pointrecharge/vo/AdminTutorPointRechargePackageRespVO.java`
- Create: `sql/mysql/tutor_point_recharge_menu.sql`
- Create: `zlwl-vue/src/api/tutor/pointRecharge/index.ts`
- Create: `zlwl-vue/src/views/tutor/pointRecharge/index.vue`

- [ ] **Step 1: Add backend CRUD controller**

Expose `/admin-api/tutor/point-recharge-package` page, create, update, update-status, and delete routes with permissions:

```text
tutor:point-recharge-package:query
tutor:point-recharge-package:create
tutor:point-recharge-package:update
tutor:point-recharge-package:delete
```

- [ ] **Step 2: Add idempotent menu SQL**

Create a “积分套餐” page below “家教管理” and button permissions below that page. Use `INSERT ... SELECT ... WHERE NOT EXISTS`.

- [ ] **Step 3: Add Vue API and page**

Build an Element Plus table and edit dialog following `src/views/tutor/city/index.vue`. Show yuan values in the UI and convert to cents in request payloads:

```ts
payPrice: Math.round(Number(form.payPriceYuan) * 100)
```

- [ ] **Step 4: Verify and commit each repo**

Run in `zlwl-vue`:

```powershell
pnpm ts:check
```

Expected: PASS.

Commit backend:

```powershell
git add yudao-module-tutor/src/main/java/cn/iocoder/yudao/module/tutor/controller/admin/pointrecharge sql/mysql/tutor_point_recharge_menu.sql
git commit -m "feat(tutor): add point package administration"
```

Commit admin frontend:

```powershell
git add src/api/tutor/pointRecharge src/views/tutor/pointRecharge
git commit -m "feat(tutor): add point package admin page"
```

## Task 6: Add Uni-App Recharge UI And WeChat Payment Invocation

**Files:**
- Create: `zfwl-unapp/sheep/api/tutor/point-recharge.js`
- Create: `zfwl-unapp/sheep/api/pay/order.js`
- Create: `zfwl-unapp/tests/tutor-point-recharge.test.cjs`
- Modify: `zfwl-unapp/pages/user/wallet/score.vue`

- [ ] **Step 1: Write failing Node regression test**

Assert the source contains:

```js
url: '/tutor/point-recharge/packages'
url: '/tutor/point-recharge/orders'
packageId
uni.requestPayment
await sheep.$store('user').getInfo()
await getLogList(true)
```

Also assert the old primary recharge action no longer routes to customer service.

- [ ] **Step 2: Run test and observe RED**

Run:

```powershell
node tests\tutor-point-recharge.test.cjs
```

Expected: FAIL because the API and purchase card do not exist.

- [ ] **Step 3: Add API wrapper**

Export:

```js
getPackageList()
createOrder(packageId)
getOrder(id)
```

The create request body must be exactly:

```js
{ packageId }
```

- [ ] **Step 4: Add pay-order submission wrapper**

Create `sheep/api/pay/order.js`:

```js
import request from '@/sheep/request';

export default {
  submitOrder(data) {
    return request({
      url: '/pay/order/submit',
      method: 'POST',
      data,
    });
  },
};
```

- [ ] **Step 5: Add score-page purchase flow**

Add a package card and:

```js
const order = await PointRechargeApi.createOrder(item.id);
const pay = await PayOrderApi.submitOrder({
  id: order.data.payOrderId,
  channelCode: 'wx_lite',
  channelExtras: { openid: await sheep.$platform.useProvider('wechat').getOpenid(true) },
});
await uni.requestPayment(JSON.parse(pay.data.displayContent));
await sheep.$store('user').getInfo();
await getLogList(true);
```

Import `PayOrderApi` from `@/sheep/api/pay/order`. The existing backend returns the mini-program payment payload as JSON in `displayContent`. On cancellation show “支付未完成”; on other failures show “支付失败，请重试”. Never mutate points locally.

- [ ] **Step 6: Verify and commit**

Run:

```powershell
node tests\tutor-point-recharge.test.cjs
node tests\tutor-point-task-loop.test.cjs
.\node_modules\.bin\prettier.cmd --check pages\user\wallet\score.vue sheep\api\tutor\point-recharge.js sheep\api\pay\order.js tests\tutor-point-recharge.test.cjs
```

Expected: PASS.

Commit:

```powershell
git add pages/user/wallet/score.vue sheep/api/tutor/point-recharge.js sheep/api/pay/order.js tests/tutor-point-recharge.test.cjs
git commit -m "feat(tutor): add wechat point recharge UI"
```

## Task 7: Add Deployment Guardrails And Verification

**Files:**
- Modify: `.gitignore`
- Create: `docs/deploy/tutor-point-recharge-wechat.md`

- [ ] **Step 1: Add ignore rules**

Append:

```gitignore
*.p12
apiclient_*.pem
*_cert.zip
```

- [ ] **Step 2: Add deployment runbook**

Document:

- Create or enable the `tutor-point-recharge` pay application.
- Add a `wx_lite` channel using the rotated merchant credentials.
- Store certificates outside the repository.
- Configure the public HTTPS callback URL.
- Apply `sql/mysql/tutor_point_recharge.sql` and `sql/mysql/tutor_point_recharge_menu.sql`.
- Test with a low-value package before production rollout.

Do not include any actual merchant number, key, certificate serial number, or private-key path from a developer machine.

- [ ] **Step 3: Run backend verification**

Run:

```powershell
mvn -pl yudao-module-tutor -am test
```

Expected: BUILD SUCCESS.

- [ ] **Step 4: Run frontend verification**

Run in `zlwl-vue`:

```powershell
pnpm ts:check
```

Run in `zfwl-unapp`:

```powershell
node tests\tutor-point-recharge.test.cjs
node tests\tutor-point-task-loop.test.cjs
.\node_modules\.bin\prettier.cmd --check pages\user\wallet\score.vue sheep\api\tutor\point-recharge.js sheep\api\pay\order.js tests\tutor-point-recharge.test.cjs
```

Expected: PASS.

- [ ] **Step 5: Scan for leaked secrets**

Run from `D:\work\jiajiao`:

```powershell
rg -n "BEGIN (RSA )?PRIVATE KEY|apiclient_(cert|key)|apiV3Key|privateKeyContent" zfwl zlwl-vue zfwl-unapp
```

Expected: no matches.

- [ ] **Step 6: Commit guardrails**

```powershell
git add .gitignore docs/deploy/tutor-point-recharge-wechat.md
git commit -m "docs: add tutor point recharge deployment guide"
```

## Final Acceptance

- [ ] Apply schema to a test database.
- [ ] Configure a rotated `wx_lite` channel outside Git.
- [ ] Create a low-value enabled package in the admin UI.
- [ ] Pay once in WeChat mini-program and verify total points increase once.
- [ ] Replay the payment notify task and verify points do not increase again.
- [ ] Cancel a payment and verify points remain unchanged.
- [ ] Disable the package and verify new orders are rejected while historical orders remain readable.
