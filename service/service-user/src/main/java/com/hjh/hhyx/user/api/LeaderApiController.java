package com.hjh.hhyx.user.api;

import com.hjh.hhyx.user.service.LeaderService;
import com.hjh.hhyx.user.service.UserService;
import com.hjh.hhyx.vo.user.LeaderAddressVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 韩
 * @version 1.0
 * 远程调用的接口
 */
@Api(tags = "团长接口")
@RestController
@RequestMapping("/api/user/leader")
public class LeaderApiController {

    @Resource
    private UserService userService;

    @Resource
    private LeaderService leaderService;

    @ApiOperation("提货点地址信息")
    @GetMapping("/inner/getUserAddressByUserId/{userId}")
    public LeaderAddressVo getUserAddressByUserId(@PathVariable(value = "userId") Long userId) {
        return userService.getLeaderAddressVoByUserId(userId);
    }
}
