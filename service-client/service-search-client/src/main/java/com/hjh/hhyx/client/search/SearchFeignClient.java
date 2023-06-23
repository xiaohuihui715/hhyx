package com.hjh.hhyx.client.search;

import com.hjh.hhyx.model.search.SkuEs;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author 韩
 * @version 1.0
 */

@FeignClient(name = "service-search")
public interface SearchFeignClient {


    /**
     * 更新商品incrHotScore
     * @param skuId
     * @return
     */
    @GetMapping("/api/search/sku/inner/incrHotScore/{skuId}")
    Boolean incrHotScore(@PathVariable("skuId") Long skuId);

    /**
     * 获取爆品商品
     * @return
     */
    @GetMapping("/api/search/sku/inner/findHotSkuList")
    List<SkuEs> findHotSkuList();


}
