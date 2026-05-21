package cn.iocoder.yudao.module.tutor.service.profile;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.tutor.controller.admin.profile.vo.AdminTutorUserProfilePageReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.profile.vo.AppTutorProfileInitReqVO;
import cn.iocoder.yudao.module.tutor.controller.app.profile.vo.AppTutorProfileLocationUpdateReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.profile.TutorUserProfileDO;

/**
 * 家教用户档案 Service。
 */
public interface TutorUserProfileService {

    /**
     * 获得用户档案。
     *
     * @param userId 会员用户编号
     * @return 用户档案；不存在时返回 null
     */
    TutorUserProfileDO getProfile(Long userId);

    /**
     * 初始化用户档案。
     *
     * @param userId 会员用户编号
     * @param reqVO 初始化请求
     * @return 用户档案
     */
    TutorUserProfileDO initProfile(Long userId, AppTutorProfileInitReqVO reqVO);

    /**
     * 更新用户定位。
     *
     * @param userId 会员用户编号
     * @param reqVO 定位请求
     * @return 用户档案
     */
    TutorUserProfileDO updateLocation(Long userId, AppTutorProfileLocationUpdateReqVO reqVO);

    PageResult<TutorUserProfileDO> getProfilePage(AdminTutorUserProfilePageReqVO reqVO);

}
