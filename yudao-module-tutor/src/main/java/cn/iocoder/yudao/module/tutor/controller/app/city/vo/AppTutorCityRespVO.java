package cn.iocoder.yudao.module.tutor.controller.app.city.vo;

import cn.iocoder.yudao.module.tutor.dal.dataobject.city.TutorCityDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "用户 App - 家教城市 Response VO")
@Data
public class AppTutorCityRespVO {

    @Schema(description = "城市编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "城市名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "上海")
    private String name;

    @Schema(description = "城市编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "310100")
    private String code;

    @Schema(description = "拼音", requiredMode = Schema.RequiredMode.REQUIRED, example = "shanghai")
    private String pinyin;

    @Schema(description = "省份", requiredMode = Schema.RequiredMode.REQUIRED, example = "上海市")
    private String province;

    @Schema(description = "是否开通", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private Boolean opened;

    @Schema(description = "是否热门城市", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private Boolean hot;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    private Integer sort;

    public static AppTutorCityRespVO of(TutorCityDO city) {
        AppTutorCityRespVO respVO = new AppTutorCityRespVO();
        respVO.setId(city.getId());
        respVO.setName(city.getName());
        respVO.setCode(city.getCode());
        respVO.setPinyin(city.getPinyin());
        respVO.setProvince(city.getProvince());
        respVO.setOpened(city.getOpened());
        respVO.setHot(city.getHot());
        respVO.setSort(city.getSort());
        return respVO;
    }

}
