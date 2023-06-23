package com.hjh.hhyx.activity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjh.hhyx.activity.mapper.ActivityInfoMapper;
import com.hjh.hhyx.activity.mapper.ActivityRuleMapper;
import com.hjh.hhyx.activity.mapper.ActivitySkuMapper;
import com.hjh.hhyx.activity.service.ActivityInfoService;
import com.hjh.hhyx.activity.service.CouponInfoService;
import com.hjh.hhyx.client.product.ProductFeignClient;
import com.hjh.hhyx.enums.ActivityType;
import com.hjh.hhyx.model.activity.ActivityInfo;
import com.hjh.hhyx.model.activity.ActivityRule;
import com.hjh.hhyx.model.activity.ActivitySku;
import com.hjh.hhyx.model.activity.CouponInfo;
import com.hjh.hhyx.model.product.SkuInfo;
import com.hjh.hhyx.vo.activity.ActivityRuleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 活动表 服务实现类
 * </p>
 *
 * @author hjh
 * @since 2023-06-19
 */
@Service
public class ActivityInfoServiceImpl extends ServiceImpl<ActivityInfoMapper, ActivityInfo> implements ActivityInfoService {

    @Autowired
    private ActivityInfoMapper activityInfoMapper;

    @Autowired
    private ActivityRuleMapper activityRuleMapper;

    @Autowired
    private ActivitySkuMapper activitySkuMapper;

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private CouponInfoService couponInfoService;

    //优惠活动列表方法
    @Override
    public IPage<ActivityInfo> selectPage(Page<ActivityInfo> pageParam) {
        QueryWrapper<ActivityInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");

        IPage<ActivityInfo> page = activityInfoMapper.selectPage(pageParam, queryWrapper);
        page.getRecords().stream().forEach(item -> {
            item.setActivityTypeString(item.getActivityType().getComment());
        });
        return page;
    }

    //活动规则列表方法
    @Override
    public Map<String, Object> findActivityRuleList(Long activityId) {
        Map<String, Object> result = new HashMap<>();

        //根据活动id获得活动规则
        QueryWrapper queryWrapper = new QueryWrapper<ActivityRule>();
        queryWrapper.eq("activity_id", activityId);
        List<ActivityRule> activityRuleList = activityRuleMapper.selectList(queryWrapper);
        result.put("activityRuleList", activityRuleList);

        //查询当前活动的sku商品
        QueryWrapper activitySkuQueryWrapper = new QueryWrapper<ActivitySku>();
        activitySkuQueryWrapper.eq("activity_id", activityId);
        List<ActivitySku> activitySkuList = activitySkuMapper.selectList(activitySkuQueryWrapper);
        List<Long> skuIdList = activitySkuList.stream().map(ActivitySku::getSkuId).collect(Collectors.toList());
        List<SkuInfo> skuInfoList = productFeignClient.findSkuInfoList(skuIdList);
        result.put("skuInfoList", skuInfoList);
        return result;
    }

    //保存活动规则
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveActivityRule(ActivityRuleVo activityRuleVo) {
        //删除之前的活动规则
        activityRuleMapper.delete(new QueryWrapper<ActivityRule>().eq("activity_id", activityRuleVo.getActivityId()));
        activitySkuMapper.delete(new QueryWrapper<ActivitySku>().eq("activity_id", activityRuleVo.getActivityId()));

        //获取新的活动规则
        List<ActivityRule> activityRuleList = activityRuleVo.getActivityRuleList();
        List<ActivitySku> activitySkuList = activityRuleVo.getActivitySkuList();
        List<Long> couponIdList = activityRuleVo.getCouponIdList();

        ActivityInfo activityInfo = activityInfoMapper.selectById(activityRuleVo.getActivityId());
        for (ActivityRule activityRule : activityRuleList) {
            activityRule.setActivityId(activityRuleVo.getActivityId());
            activityRule.setActivityType(activityInfo.getActivityType());
            activityRuleMapper.insert(activityRule);
        }

        for (ActivitySku activitySku : activitySkuList) {
            activitySku.setActivityId(activityRuleVo.getActivityId());
            activitySkuMapper.insert(activitySku);
        }
    }

    //根据关键字查询sku信息列表（没有参加活动的sku，因为一个sku只能参加一个活动）
    @Override
    public List<SkuInfo> findSkuInfoByKeyword(String keyword) {
        List<SkuInfo> skuInfoList = productFeignClient.findSkuInfoByKeyword(keyword);
        List<Long> skuIdList = skuInfoList.stream().map(SkuInfo::getId).collect(Collectors.toList());

        List<SkuInfo> notExistSkuInfoList = new ArrayList<>();
        //已经存在的skuId，一个sku只能参加一个促销活动，所以存在的得排除
        List<Long> existSkuIdList = activityInfoMapper.selectExistSkuIdList(skuIdList);
        for (SkuInfo skuInfo : skuInfoList) {
            if (!existSkuIdList.contains(skuInfo.getId())) {
                notExistSkuInfoList.add(skuInfo);
            }
        }
        return notExistSkuInfoList;
    }

    //查询商品获取规则数据
    @Override
    public List<ActivityRule> findActivityRule(Long skuId) {
        List<ActivityRule> activityRuleList = activityInfoMapper.selectActivityRuleList(skuId);
        if (!CollectionUtils.isEmpty(activityRuleList)) {
            for (ActivityRule activityRule : activityRuleList) {
                activityRule.setRuleDesc(this.getRuleDesc(activityRule));
            }
        }
        return activityRuleList;
    }

    //根据skuId列表获取促销信息
    @Override
    public Map<Long, List<String>> findActivity(List<Long> skuIdList) {
        Map<Long, List<String>> result = new HashMap<>();
        //skuIdList遍历，得到每个skuId
        skuIdList.forEach(skuId -> {
            //根据skuId进行查询，查询sku对应活动里面规则列表
            List<ActivityRule> activityRuleList =
                    baseMapper.selectActivityRuleList(skuId);
            //数据封装，规则名称
            if (!CollectionUtils.isEmpty(activityRuleList)) {
                List<String> ruleList = new ArrayList<>();
                //把规则名称处理
                for (ActivityRule activityRule : activityRuleList) {
                    ruleList.add(this.getRuleDesc(activityRule));
                }
                result.put(skuId, ruleList);
            }
        });
        return result;
    }

    //构造规则名称的方法
    private String getRuleDesc(ActivityRule activityRule) {
        ActivityType activityType = activityRule.getActivityType();
        StringBuffer ruleDesc = new StringBuffer();
        if (activityType == ActivityType.FULL_REDUCTION) {
            ruleDesc
                    .append("满")
                    .append(activityRule.getConditionAmount())
                    .append("元减")
                    .append(activityRule.getBenefitAmount())
                    .append("元");
        } else {
            ruleDesc
                    .append("满")
                    .append(activityRule.getConditionNum())
                    .append("元打")
                    .append(activityRule.getBenefitDiscount())
                    .append("折");
        }
        return ruleDesc.toString();
    }


    @Override
    public Map<String, Object> findActivityAndCoupon(Long skuId, Long userId) {
        //一个sku只能有一个促销活动，一个活动有多个活动规则（如满赠，满100送10，满500送50）
        List<ActivityRule> activityRuleList = this.findActivityRule(skuId);

        //获取优惠券信息
        List<CouponInfo> couponInfoList = couponInfoService.findCouponInfo(skuId, userId);

        Map<String, Object> map = new HashMap<>();
        map.put("activityRuleList", activityRuleList);
        map.put("couponInfoList", couponInfoList);
        return map;
    }
}
