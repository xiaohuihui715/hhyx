package com.hjh.hhyx.search.service;

import com.hjh.hhyx.model.search.SkuEs;
import com.hjh.hhyx.vo.search.SkuEsQueryVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

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

    List<SkuEs> finHotSkuList();

    /**
     * 搜索列表
     * @param pageable
     * @param searchParamVo
     * @return
     */
    Page<SkuEs> search(Pageable pageable, SkuEsQueryVo searchParamVo);

    //更新商品热度
    void incrHotScore(Long skuId);
}
