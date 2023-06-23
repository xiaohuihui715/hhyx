package com.hjh.hhyx.search.service.impl;

import com.alibaba.fastjson2.JSON;
import com.hjh.hhyx.client.activity.ActivityFeignClient;
import com.hjh.hhyx.client.product.ProductFeignClient;
import com.hjh.hhyx.common.auth.AuthContextHolder;
import com.hjh.hhyx.enums.SkuType;
import com.hjh.hhyx.model.product.Category;
import com.hjh.hhyx.model.product.SkuInfo;
import com.hjh.hhyx.model.search.SkuEs;
import com.hjh.hhyx.search.repository.SkuSearchRepository;
import com.hjh.hhyx.search.service.SkuSearchService;
import com.hjh.hhyx.vo.search.SkuEsQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author 韩
 * @version 1.0
 */
@Service
@Slf4j
public class SkuSearchServiceImpl implements SkuSearchService {
    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private SkuSearchRepository skuSearchRepository;

    @Autowired
    private ActivityFeignClient activityFeignClient;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 上架商品列表
     *
     * @param skuId
     */
    @Override
    public void upperSku(Long skuId) {
        log.info("upperSku：" + skuId);
        SkuEs skuEs = new SkuEs();

        //查询sku信息
        SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
        if (null == skuInfo) return;

        // 查询分类
        Category category = productFeignClient.getCategory(skuInfo.getCategoryId());
        if (category != null) {
            skuEs.setCategoryId(category.getId());
            skuEs.setCategoryName(category.getName());
        }
        skuEs.setId(skuInfo.getId());
        skuEs.setKeyword(skuInfo.getSkuName() + "," + skuEs.getCategoryName());
        skuEs.setWareId(skuInfo.getWareId());
        skuEs.setIsNewPerson(skuInfo.getIsNewPerson());
        skuEs.setImgUrl(skuInfo.getImgUrl());
        skuEs.setTitle(skuInfo.getSkuName());
        if (skuInfo.getSkuType() == SkuType.COMMON.getCode()) {
            skuEs.setSkuType(0);
            skuEs.setPrice(skuInfo.getPrice().doubleValue());
            skuEs.setStock(skuInfo.getStock());
            skuEs.setSale(skuInfo.getSale());
            skuEs.setPerLimit(skuInfo.getPerLimit());
        } else {
            //TODO 待完善-秒杀商品

        }
        SkuEs save = skuSearchRepository.save(skuEs);
        log.info("upperSku：" + JSON.toJSONString(save));
    }

    /**
     * 下架商品列表
     *
     * @param skuId
     */
    @Override
    public void lowerSku(Long skuId) {
        this.skuSearchRepository.deleteById(skuId);
    }

    @Override
    public List<SkuEs> finHotSkuList() {
        Pageable page = PageRequest.of(0, 10);
        //使用Spring Data查询，即不用自己写查询方法，只需要按规定名称编写接口即可
        Page<SkuEs> pageModel = skuSearchRepository.findByOrderByHotScoreDesc(page);
        List<SkuEs> skuEsList = pageModel.getContent();
        return skuEsList;
    }

    @Override
    public Page<SkuEs> search(Pageable pageable, SkuEsQueryVo skuEsQueryVo) {
        //获取到当前用户所在地址仓库的id
        skuEsQueryVo.setWareId(AuthContextHolder.getWareId());
        Page<SkuEs> page = null;
        if (StringUtils.isEmpty(skuEsQueryVo.getKeyword())) {
            page = skuSearchRepository.findByCategoryIdAndWareId(skuEsQueryVo.getCategoryId(), skuEsQueryVo.getWareId(), pageable);
        } else {
            page = skuSearchRepository.findByKeywordAndWareId(skuEsQueryVo.getKeyword(), skuEsQueryVo.getWareId(), pageable);
        }

        List<SkuEs> skuEsList = page.getContent();
        //获取sku对应的促销活动标签
        if (!CollectionUtils.isEmpty(skuEsList)) {
            List<Long> skuIdList = skuEsList.stream().map(sku -> sku.getId()).collect(Collectors.toList());
            Map<Long, List<String>> skuIdToRuleListMap = activityFeignClient.findActivity(skuIdList);
            if (null != skuIdToRuleListMap) {
                skuEsList.forEach(skuEs -> {
                    skuEs.setRuleList(skuIdToRuleListMap.get(skuEs.getId()));
                });
            }
        }
        return page;
    }

    @Override
    public void incrHotScore(Long skuId) {
        // 定义key
        String hotKey = "hotScore";
        // 保存数据
        Double hotScore = redisTemplate.opsForZSet().incrementScore(hotKey, "skuId:" + skuId, 1);
        if (hotScore % 10 == 0) {
            // 更新es
            Optional<SkuEs> optional = skuSearchRepository.findById(skuId);
            SkuEs skuEs = optional.get();
            skuEs.setHotScore(Math.round(hotScore));
            skuSearchRepository.save(skuEs);
        }
    }
}
