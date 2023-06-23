package com.hjh.hhyx.search.api;

import com.hjh.hhyx.common.result.Result;
import com.hjh.hhyx.model.search.SkuEs;
import com.hjh.hhyx.search.service.SkuSearchService;
import com.hjh.hhyx.vo.search.SkuEsQueryVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 韩
 * @version 1.0
 */

@RestController
@RequestMapping("/api/search/sku")
public class SkuSearchApiController {

    @Autowired
    private SkuSearchService skuSearchService;

    //获取爆品
    @GetMapping("/inner/findHotSkuList")
    public List<SkuEs> findHotSkuList() {
        return skuSearchService.finHotSkuList();
    }

    @ApiOperation(value = "搜索商品")
    @GetMapping("{page}/{limit}")
    public Result list(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Integer page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Integer limit,
            @ApiParam(name = "searchParamVo", value = "查询对象", required = false)
            SkuEsQueryVo searchParamVo) {

        Pageable pageable = PageRequest.of(page-1, limit);
        Page<SkuEs> pageModel =  skuSearchService.search(pageable, searchParamVo);
        return Result.ok(pageModel);
    }


    @ApiOperation(value = "更新商品incrHotScore")
    @GetMapping("inner/incrHotScore/{skuId}")
    public Boolean incrHotScore(@PathVariable("skuId") Long skuId) {
        // 调用服务层
        skuSearchService.incrHotScore(skuId);
        return true;
    }
}
