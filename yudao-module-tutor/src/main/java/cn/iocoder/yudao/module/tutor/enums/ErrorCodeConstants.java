package cn.iocoder.yudao.module.tutor.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * Tutor 错误码枚举类。
 *
 * tutor 系统，使用 1-040-000-000 段。
 */
public interface ErrorCodeConstants {

    // ========== 城市 1-040-001-000 ==========
    ErrorCode CITY_NOT_EXISTS = new ErrorCode(1_040_001_000, "城市不存在");
    ErrorCode CITY_NOT_OPENED = new ErrorCode(1_040_001_001, "该城市暂未开通");

    // ========== 用户档案 1-040-002-000 ==========
    ErrorCode PROFILE_NOT_EXISTS = new ErrorCode(1_040_002_000, "家教用户档案不存在");
    ErrorCode PROFILE_ROLE_SELECTED = new ErrorCode(1_040_002_001, "用户身份已选择，如需修改请发起身份变更申请");
    ErrorCode PROFILE_ROLE_INVALID = new ErrorCode(1_040_002_002, "用户身份不正确");
    ErrorCode PROFILE_ROLE_NOT_PARENT = new ErrorCode(1_040_002_003, "当前身份不是家长，无法维护家长资料");
    ErrorCode PROFILE_ROLE_NOT_TEACHER = new ErrorCode(1_040_002_004, "当前身份不是教师，无法维护教师资料");

    // ========== 家长资料 1-040-002-100 ==========
    ErrorCode PARENT_PROFILE_NOT_EXISTS = new ErrorCode(1_040_002_100, "家长资料不存在");

    // ========== 教师资料 1-040-002-200 ==========
    ErrorCode TEACHER_PROFILE_NOT_EXISTS = new ErrorCode(1_040_002_200, "教师资料不存在");

    // ========== 教师认证 1-040-003-000 ==========
    ErrorCode CERTIFICATION_NOT_EXISTS = new ErrorCode(1_040_003_000, "教师认证不存在");
    ErrorCode CERTIFICATION_PENDING = new ErrorCode(1_040_003_001, "教师认证审核中，请勿重复提交");
    ErrorCode CERTIFICATION_NOT_APPROVED = new ErrorCode(1_040_003_002, "教师认证未通过，暂不能发布授课简历");
    ErrorCode CERTIFICATION_REJECT_REASON_REQUIRED = new ErrorCode(1_040_003_003, "拒绝教师认证时必须填写原因");

    // ========== 发布内容 1-040-004-000 ==========
    ErrorCode DEMAND_NOT_EXISTS = new ErrorCode(1_040_004_000, "家长需求不存在");
    ErrorCode RESUME_NOT_EXISTS = new ErrorCode(1_040_004_001, "教师简历不存在");
    ErrorCode PUBLISH_COUNT_EXCEED = new ErrorCode(1_040_004_002, "同时最多发布 3 条有效内容");
    ErrorCode PUBLISH_AUDIT_REJECT_REASON_REQUIRED = new ErrorCode(1_040_004_003, "拒绝发布内容时必须填写原因");
    ErrorCode PUBLISH_STATUS_NOT_VISIBLE = new ErrorCode(1_040_004_004, "发布内容暂不可见");
    ErrorCode PUBLISH_OPERATION_FORBIDDEN = new ErrorCode(1_040_004_005, "无权操作该发布内容");

    // ========== 联系与匹配 1-040-005-000 ==========
    ErrorCode CONTACT_TARGET_NOT_EXISTS = new ErrorCode(1_040_005_000, "联系方式目标不存在");
    ErrorCode CONTACT_POINT_NOT_ENOUGH = new ErrorCode(1_040_005_001, "积分不足，无法查看联系方式");
    ErrorCode MATCH_NOT_EXISTS = new ErrorCode(1_040_005_002, "匹配记录不存在");
    ErrorCode MATCH_CONFIRM_FORBIDDEN = new ErrorCode(1_040_005_003, "无权确认该匹配记录");

    // ========== 互动 1-040-006-000 ==========
    ErrorCode FAVORITE_COUNT_EXCEED = new ErrorCode(1_040_006_000, "收藏数量已达上限");
    ErrorCode REVIEW_NOT_ALLOWED = new ErrorCode(1_040_006_001, "当前匹配状态暂不可评价");
    ErrorCode REVIEW_EXISTS = new ErrorCode(1_040_006_002, "已评价，请勿重复提交");
    ErrorCode REVIEW_NOT_EXISTS = new ErrorCode(1_040_006_003, "评价不存在");

    // ========== 积分充值 1-040-007-000 ==========
    ErrorCode POINT_PACKAGE_NOT_EXISTS = new ErrorCode(1_040_007_000, "积分包不存在或已停用");
    ErrorCode POINT_RECHARGE_ORDER_NOT_EXISTS = new ErrorCode(1_040_007_001, "积分充值订单不存在");

    // ========== 邀请 1-040-008-000 ==========
    ErrorCode INVITE_CODE_NOT_EXISTS = new ErrorCode(1_040_008_000, "邀请码不存在或已失效");

    // ========== 增值服务 1-040-009-000 ==========
    ErrorCode VALUE_SERVICE_CONFIG_NOT_EXISTS = new ErrorCode(1_040_009_000, "增值服务配置不存在或已停用");
    ErrorCode VALUE_SERVICE_TARGET_TYPE_NOT_SUPPORT = new ErrorCode(1_040_009_001, "增值服务目标类型不支持");
    ErrorCode VALUE_SERVICE_TARGET_NOT_EXISTS = new ErrorCode(1_040_009_002, "增值服务目标不存在");
    ErrorCode VALUE_SERVICE_POINT_NOT_ENOUGH = new ErrorCode(1_040_009_003, "积分不足，无法购买增值服务");

}
