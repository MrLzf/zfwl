package cn.iocoder.yudao.module.tutor.service.point;

import cn.iocoder.yudao.module.tutor.enums.point.TutorPointTaskTypeEnum;

public interface TutorPointRewardService {

    boolean reward(Long userId, TutorPointTaskTypeEnum taskType, String bizId, String remark);

    boolean hasReward(Long userId, TutorPointTaskTypeEnum taskType);

}
