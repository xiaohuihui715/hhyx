package com.hjh.hhyx.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hjh.hhyx.model.user.User;
import com.hjh.hhyx.vo.user.LeaderAddressVo;
import com.hjh.hhyx.vo.user.UserLoginVo;

/**
 * @author 韩
 * @version 1.0
 */
public interface UserService extends IService<User> {

    /**
     * 根据用户id获取团长和提货人信息
     * @param userId
     * @return
     */
    LeaderAddressVo getLeaderAddressVoByUserId(Long userId);

    /**
     * 根据微信openid获取用户信息
     * @param openId
     * @return
     */
    User getByOpenid(String openId);

    /**
     * 获取当前登录用户信息
     * @param userId
     * @return
     */
    UserLoginVo getUserLoginVo(Long userId);
}
