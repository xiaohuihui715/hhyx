package com.hjh.hhyx.home.controller;

import com.hjh.hhyx.common.auth.AuthContextHolder;
import com.hjh.hhyx.common.result.Result;
import com.hjh.hhyx.home.service.HomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author 韩
 * @version 1.0
 */
@Api(tags = "首页接口")
@RestController
@RequestMapping("api/home")
public class HomeApiController {

    @Resource
    private HomeService homeService;

    @ApiOperation(value = "获取首页数据")
    @GetMapping("index")
    public Result index(HttpServletRequest request) {
        // 获取用户Id
        Long userId = AuthContextHolder.getUserId();
        return Result.ok(homeService.home(userId));
    }


}
