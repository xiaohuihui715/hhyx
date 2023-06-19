package com.hjh.hhyx.search.service;

/**
 * @author 韩
 * @version 1.0
 */

public interface SkuSearchService {
    /**
     * 上架商品
     * @param skuId
     */
    void upperSku(Long skuId);

    /**
     * 下架商品列表
     * @param skuId
     */
    void lowerSku(Long skuId);
}
