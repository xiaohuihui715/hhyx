package com.hjh.hhyx.search.service.impl;

import com.alibaba.fastjson2.JSON;
import com.hjh.hhyx.client.product.ProductFeignClient;
import com.hjh.hhyx.enums.SkuType;
import com.hjh.hhyx.model.product.Category;
import com.hjh.hhyx.model.product.SkuInfo;
import com.hjh.hhyx.model.search.SkuEs;
import com.hjh.hhyx.search.repository.SkuSearchRepository;
import com.hjh.hhyx.search.service.SkuSearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private RestHighLevelClient restHighLevelClient;

    /**
     * 上架商品列表
     * @param skuId
     */
    @Override
    public void upperSku(Long skuId) {
        log.info("upperSku："+skuId);
        SkuEs skuEs = new SkuEs();

        //查询sku信息
        SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
        if(null == skuInfo) return;

        // 查询分类
        Category category = productFeignClient.getCategory(skuInfo.getCategoryId());
        if (category != null) {
            skuEs.setCategoryId(category.getId());
            skuEs.setCategoryName(category.getName());
        }
        skuEs.setId(skuInfo.getId());
        skuEs.setKeyword(skuInfo.getSkuName()+","+skuEs.getCategoryName());
        skuEs.setWareId(skuInfo.getWareId());
        skuEs.setIsNewPerson(skuInfo.getIsNewPerson());
        skuEs.setImgUrl(skuInfo.getImgUrl());
        skuEs.setTitle(skuInfo.getSkuName());
        if(skuInfo.getSkuType() == SkuType.COMMON.getCode()) {
            skuEs.setSkuType(0);
            skuEs.setPrice(skuInfo.getPrice().doubleValue());
            skuEs.setStock(skuInfo.getStock());
            skuEs.setSale(skuInfo.getSale());
            skuEs.setPerLimit(skuInfo.getPerLimit());
        } else {
            //TODO 待完善-秒杀商品

        }
        SkuEs save = skuSearchRepository.save(skuEs);
        log.info("upperSku："+ JSON.toJSONString(save));
    }

    /**
     * 下架商品列表
     * @param skuId
     */
    @Override
    public void lowerSku(Long skuId) {
        this.skuSearchRepository.deleteById(skuId);
    }
}
