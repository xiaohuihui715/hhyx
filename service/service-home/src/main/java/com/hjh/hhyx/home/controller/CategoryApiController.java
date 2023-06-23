package com.hjh.hhyx.home.controller;

import com.hjh.hhyx.client.product.ProductFeignClient;
import com.hjh.hhyx.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 韩
 * @version 1.0
 */
@Api(tags = "商品分类")
@RestController
@RequestMapping("api/home")
public class CategoryApiController {

    @Resource
    private ProductFeignClient productFeignClient;

    @ApiOperation(value = "获取分类信息")
    @GetMapping("category")
    public Result index() {
        return Result.ok(productFeignClient.findAllCategoryList());
    }
}
