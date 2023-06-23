package com.hjh.hhyx.client.user;

import com.hjh.hhyx.vo.user.LeaderAddressVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author 韩
 * @version 1.0
 */
@FeignClient(value = "service-user")
public interface UserFeignClient {

    //获取用户取货地址
    @GetMapping("/api/user/leader/inner/getUserAddressByUserId/{userId}")
    public LeaderAddressVo getUserAddressByUserId(@PathVariable(value = "userId") Long userId);
}
