package com.hjh.hhyx.activity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hjh.hhyx.model.activity.CouponInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 优惠券信息 Mapper 接口
 * </p>
 *
 * @author hjh
 * @since 2023-06-19
 */
@Repository
public interface CouponInfoMapper extends BaseMapper<CouponInfo> {


    /**
     * sku优惠券
     * @param skuId
     * @param categoryId
     * @param userId
     * @return
     */
    List<CouponInfo> selectCouponInfoList(@Param("skuId") Long skuId, @Param("categoryId") Long categoryId, @Param("userId") Long userId);
}
