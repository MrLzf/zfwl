package cn.iocoder.yudao.module.member.service.auth;

import cn.iocoder.yudao.module.member.controller.app.auth.vo.AppAuthTutorProfileRespVO;

/**
 * 会员登录扩展的家教档案能力。
 *
 * <p>接口定义在 member 模块，tutor 模块按需实现，避免 member 与 tutor 形成 Maven 循环依赖。</p>
 */
public interface MemberTutorProfileApi {

    /**
     * 获得家教档案。
     *
     * @param userId 会员用户编号
     * @return 家教档案；不存在时返回 null
     */
    AppAuthTutorProfileRespVO getProfile(Long userId);

    /**
     * 幂等初始化家教档案。
     *
     * @param userId 会员用户编号
     * @param role 身份：1 家长，2 教师
     * @param cityCode 城市编码
     * @return 家教档案
     */
    AppAuthTutorProfileRespVO initProfileIfAbsent(Long userId, Integer role, String cityCode);

}
