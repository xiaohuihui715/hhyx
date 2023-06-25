package com.hjh.hhyx.payment.controller;

import com.hjh.hhyx.common.result.Result;
import com.hjh.hhyx.common.result.ResultCodeEnum;
import com.hjh.hhyx.enums.PaymentType;
import com.hjh.hhyx.payment.service.PaymentService;
import com.hjh.hhyx.payment.service.WeixinService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author 韩
 * @version 1.0
 */
@Api(tags = "微信支付接口")
@RestController
@RequestMapping("/api/payment/weixin")
@Slf4j
public class WeixinController {

    @Autowired
    private WeixinService weixinPayService;

    @Autowired
    private PaymentService paymentService;

    @ApiOperation(value = "下单 小程序支付")
    @GetMapping("/createJsapi/{orderNo}")
    public Result createJsapi(
            @ApiParam(name = "orderNo", value = "订单No", required = true)
            @PathVariable("orderNo") String orderNo) {
        return Result.ok(weixinPayService.createJsapi(orderNo));
    }

    @ApiOperation(value = "查询支付状态")
    @GetMapping("/queryPayStatus/{orderNo}")
    public Result queryPayStatus(
            @ApiParam(name = "orderNo", value = "订单No", required = true)
            @PathVariable("orderNo") String orderNo) {
        //调用查询接口查询订单支付状态
        Map<String, String> resultMap = weixinPayService.queryPayStatus(orderNo, PaymentType.WEIXIN.name());
        if (resultMap == null) {//出错
            return Result.build(null, ResultCodeEnum.PAYMENT_FAIL);
        }
        if ("SUCCESS".equals(resultMap.get("trade_state"))) {//如果成功
            //更改订单状态，处理支付结果
            String out_trade_no = resultMap.get("out_trade_no");
            paymentService.paySuccess(out_trade_no, PaymentType.WEIXIN, resultMap);
            return Result.build(null,ResultCodeEnum.PAYMENT_SUCCESS);
        }
        return Result.build(null,ResultCodeEnum.PAYMENT_WAITING);
    }
}
