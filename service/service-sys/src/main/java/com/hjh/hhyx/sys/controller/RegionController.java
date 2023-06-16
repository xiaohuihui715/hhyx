package com.hjh.hhyx.sys.controller;


import com.hjh.hhyx.common.result.Result;
import com.hjh.hhyx.sys.service.RegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 地区表 前端控制器
 * </p>
 *
 * @author hjh
 * @since 2023-06-15
 */
@Api(tags = "区域接口")
@RestController
@RequestMapping("/admin/sys/region")
@CrossOrigin
public class RegionController {

    @Autowired
    private RegionService regionService;

    @ApiOperation(value = "根据关键字获取地区列表")
    @GetMapping("findRegionByKeyword/{keyword}")
    public Result findSkuInfoByKeyword(@PathVariable("keyword") String keyword) {
        return Result.ok(regionService.findRegionByKeyword(keyword));
    }
}

