package com.hjh.hhyx.activity.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hjh.hhyx.model.activity.CouponInfo;
import com.hjh.hhyx.model.order.CartInfo;
import com.hjh.hhyx.vo.activity.CouponRuleVo;
import java.util.Map;

import java.util.List;

/**
 * <p>
 * 优惠券信息 服务类
 * </p>
 *
 * @author hjh
 * @since 2023-06-19
 */
public interface CouponInfoService extends IService<CouponInfo> {

    //优惠卷分页查询
    IPage<CouponInfo> selectPage(Page<CouponInfo> pageParam);

    //根据id获取优惠券
    CouponInfo getCouponInfo(String id);

    //根据优惠卷id获取优惠券规则列表
    Map<String, Object> findCouponRuleList(Long couponId);

    //新增优惠券规则
    void saveCouponRule(CouponRuleVo couponRuleVo);

    //根据关键字获取sku列表，活动使用
    List<CouponInfo> findCouponByKeyword(String keyword);


    //根据skuid和userId获取优惠卷信息
    List<CouponInfo> findCouponInfo(Long skuId, Long userId);

    //获取购物车可以使用的优惠卷和优惠卷列表
    List<CouponInfo> findCartCouponInfo(List<CartInfo> cartInfoList, Long userId);
}
