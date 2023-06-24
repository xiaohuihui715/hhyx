package com.hjh.hhyx.activity.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hjh.hhyx.model.activity.ActivityInfo;
import com.hjh.hhyx.model.activity.ActivityRule;
import com.hjh.hhyx.model.order.CartInfo;
import com.hjh.hhyx.model.product.SkuInfo;
import com.hjh.hhyx.vo.activity.ActivityRuleVo;
import com.hjh.hhyx.vo.order.CartInfoVo;
import com.hjh.hhyx.vo.order.OrderConfirmVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 活动表 服务类
 * </p>
 *
 * @author hjh
 * @since 2023-06-19
 */
public interface ActivityInfoService extends IService<ActivityInfo> {

    /**
     * 获取活动分页列表
     * @param pageParam
     * @return
     */
    IPage<ActivityInfo> selectPage(Page<ActivityInfo> pageParam);

    /**
     * 获取活动规则id
     * @param activityId
     * @return
     */
    Map<String, Object> findActivityRuleList(Long activityId);

    //保存活动规则信息
    void saveActivityRule(ActivityRuleVo activityRuleVo);

    //根据关键字获取sku信息列表
    List<SkuInfo> findSkuInfoByKeyword(String keyword);

    /**
     * 根据skuId获取促销规则信息
     * @param skuId
     * @return
     */
    List<ActivityRule> findActivityRule(Long skuId);

    //根据skuId列表获取促销信息
    Map<Long, List<String>> findActivity(List<Long> skuIdList);

    //根据skuid获取活动和优惠卷信息
    Map<String, Object> findActivityAndCoupon(Long skuId, Long userId);

    OrderConfirmVo findCartActivityAndCoupon(List<CartInfo> cartInfoList, Long userId);

    //获取购物车每个活动对应的商品规则数据
    List<CartInfoVo> findCartActivityList(List<CartInfo> cartInfoList);
}
