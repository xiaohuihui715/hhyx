package com.hjh.hhyx.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.hjh.hhyx.common.auth.AuthContextHolder;
import com.hjh.hhyx.common.constant.RedisConst;
import com.hjh.hhyx.common.exception.HhyxException;
import com.hjh.hhyx.common.result.Result;
import com.hjh.hhyx.common.result.ResultCodeEnum;
import com.hjh.hhyx.common.utils.JwtHelper;
import com.hjh.hhyx.enums.UserType;
import com.hjh.hhyx.model.user.User;
import com.hjh.hhyx.user.service.UserService;
import com.hjh.hhyx.user.utils.ConstantPropertiesUtil;
import com.hjh.hhyx.user.utils.HttpClientUtils;
import com.hjh.hhyx.vo.user.LeaderAddressVo;
import com.hjh.hhyx.vo.user.UserLoginVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 韩
 * @version 1.0
 */

@RestController
@RequestMapping("/api/user/weixin")
public class WeixinApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation(value = "微信登录获取openid(小程序)")
    @GetMapping("/wxLogin/{code}")
    public Result callback(@PathVariable String code) {
        //1. 获取授权临时票据
        System.out.println("微信授权服务器回调。。。。。。" + code);
        if (StringUtils.isEmpty(code)) {
            throw new HhyxException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }

        //2. 请求微信服务接口，使用code和appid以及appscrect换取access_token
        StringBuffer baseAccessTokenUrl = new StringBuffer()
                .append("https://api.weixin.qq.com/sns/jscode2session")
                .append("?appid=%s")
                .append("&secret=%s")
                .append("&js_code=%s")
                .append("&grant_type=authorization_code");

        String wxOpenAppId = ConstantPropertiesUtil.WX_OPEN_APP_ID;
        String wxOpenAppSecret = ConstantPropertiesUtil.WX_OPEN_APP_SECRET;
        String accessTokenUrl = String.format(baseAccessTokenUrl.toString(),
                wxOpenAppId,
                wxOpenAppSecret,
                code);

        String result = null;
        try {
            result = HttpClientUtils.get(accessTokenUrl);//发送wx服务接口
        } catch (Exception e) {
            throw new HhyxException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }

        System.out.println("使用code换取的access_token结果 = " + result);
        JSONObject resultJson = JSONObject.parseObject(result);
        if (resultJson.getString("errcode") != null) {
            throw new HhyxException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }

        String accessToken = resultJson.getString("session_key");
        String openId = resultJson.getString("openid");

        //TODO 为了测试，openId写固定
//        String accessToken = "";
//        String openId = "odo3j4uGJf6Hl2FopkEOLGxr7LE4";

        //根据access_token获取微信用户的基本信息
        //先根据openid进行数据库查询
        User user = userService.getByOpenid(openId);
        //4. 如果没有查到用户信息,那么调用微信个人信息获取的接口
        if (user == null) {
            user = new User();
            user.setOpenId(openId);
            user.setNickName(openId);
            user.setPhotoUrl("");
            user.setUserType(UserType.USER);
            user.setIsNew(0);
            userService.save(user);
        }
        //5. 根据userId查询提货点和团长信息
        LeaderAddressVo leaderAddressVo = userService.getLeaderAddressVoByUserId(user.getId());
        Map<String, Object> map = new HashMap<>();
        String name = user.getNickName();
        map.put("user", user);
        map.put("leaderAddressVo", leaderAddressVo);
        //6. 使用Jwt工具生成token字符串
        String token = JwtHelper.createToken(user.getId(), name);
        map.put("token", token);
//        if(user.getUserType() == UserType.LEADER) {
//            Leader leader = leaderService.getLeader();
//            map.put("leader", leader);
//        }
        //7. 获取当前登录用户信息，放到redis中，设置有效时间
        UserLoginVo userLoginVo = this.userService.getUserLoginVo(user.getId());
        redisTemplate.opsForValue().set(RedisConst.USER_LOGIN_KEY_PREFIX + user.getId(), userLoginVo, RedisConst.USERKEY_TIMEOUT, TimeUnit.DAYS);
        return Result.ok(map);
    }

    @PostMapping("/auth/updateUser")
    @ApiOperation(value = "更新用户昵称与头像")
    public Result updateUser(@RequestBody User user) {
        User user1 = userService.getById(AuthContextHolder.getUserId());
        //把昵称更新为微信用户
        user1.setNickName(user.getNickName().replaceAll("[ue000-uefff]", "*"));
        user1.setPhotoUrl(user.getPhotoUrl());
        userService.updateById(user1);
        return Result.ok(null);
    }
}
