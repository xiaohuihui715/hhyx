package com.hjh.hhyx.search.controller;

import com.hjh.hhyx.common.result.Result;
import com.hjh.hhyx.search.service.SkuSearchService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 韩
 * @version 1.0
 */
@RestController
@RequestMapping("api/search/sku")

public class SkuSearchController {
    @Autowired
    private SkuSearchService skuSearchService;

    @ApiOperation(value = "上架商品")
    @GetMapping("inner/upperSku/{skuId}")
    public Result upperGoods(@PathVariable("skuId") Long skuId) {
        skuSearchService.upperSku(skuId);
        return Result.ok(null);
    }

    @ApiOperation(value = "下架商品")
    @GetMapping("inner/lowerSku/{skuId}")
    public Result lowerGoods(@PathVariable("skuId") Long skuId) {
        skuSearchService.lowerSku(skuId);
        return Result.ok(null);
    }
}
