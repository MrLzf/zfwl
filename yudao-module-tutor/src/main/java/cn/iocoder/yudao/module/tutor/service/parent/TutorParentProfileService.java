package cn.iocoder.yudao.module.tutor.service.parent;

import cn.iocoder.yudao.module.tutor.controller.app.parent.vo.AppTutorParentProfileSaveReqVO;
import cn.iocoder.yudao.module.tutor.dal.dataobject.parent.TutorParentProfileDO;

/**
 * 家长资料 Service。
 */
public interface TutorParentProfileService {

    /**
     * 获得家长资料。
     *
     * @param userId 会员用户编号
     * @return 家长资料；不存在时返回 null
     */
    TutorParentProfileDO getParentProfile(Long userId);

    /**
     * 保存家长资料。
     *
     * @param userId 会员用户编号
     * @param reqVO 保存请求
     * @return 家长资料
     */
    TutorParentProfileDO saveParentProfile(Long userId, AppTutorParentProfileSaveReqVO reqVO);

}
