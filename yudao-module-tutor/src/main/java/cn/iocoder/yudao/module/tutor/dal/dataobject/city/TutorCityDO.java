package cn.iocoder.yudao.module.tutor.dal.dataobject.city;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 家教城市 DO。
 */
@TableName("tutor_city")
@KeySequence("tutor_city_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TenantIgnore
public class TutorCityDO extends BaseDO {

    /**
     * 编号。
     */
    @TableId
    private Long id;
    /**
     * 城市名称。
     */
    private String name;
    /**
     * 城市编码。
     */
    private String code;
    /**
     * 拼音。
     */
    private String pinyin;
    /**
     * 省份。
     */
    private String province;
    /**
     * 是否开通。
     */
    private Boolean opened;
    /**
     * 是否热门城市。
     */
    private Boolean hot;
    /**
     * 城市运营配置 JSON。
     */
    private String serviceConfig;
    /**
     * 排序。
     */
    private Integer sort;
    /**
     * 状态。
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;

}
