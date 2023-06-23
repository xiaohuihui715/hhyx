package com.hjh.hhyx.activity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hjh.hhyx.model.activity.ActivityInfo;
import com.hjh.hhyx.model.activity.ActivityRule;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 活动表 Mapper 接口
 * </p>
 *
 * @author hjh
 * @since 2023-06-19
 */
@Repository
public interface ActivityInfoMapper extends BaseMapper<ActivityInfo> {

    List<Long> selectExistSkuIdList(@Param("skuIdList") List<Long> skuIdList);

//    List<ActivityRule> selectActivityRuleList(@Param("skuId") Long skuId);

    List<ActivityRule> selectActivityRuleList(@Param("skuId") Long skuId);
}
