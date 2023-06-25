package com.hjh.hhyx.payment.service.impl;

import com.alibaba.fastjson2.JSON;
import com.github.wxpay.sdk.WXPayUtil;
import com.hjh.hhyx.enums.PaymentType;
import com.hjh.hhyx.model.order.PaymentInfo;
import com.hjh.hhyx.payment.service.PaymentService;
import com.hjh.hhyx.payment.service.WeixinService;
import com.hjh.hhyx.payment.utils.ConstantPropertiesUtils;
import com.hjh.hhyx.payment.utils.HttpClient;
import com.hjh.hhyx.vo.user.UserLoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 韩
 * @version 1.0
 */
@Service
@Slf4j
public class WeixinServiceImpl implements WeixinService {

    @Resource
    private PaymentService paymentService;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 根据订单号下单，生成支付链接
     * @param orderNo
     * @return
     */
    @Override
    public Map<String, String> createJsapi(String orderNo) {
        try {
//			Map payMap = (Map) redisTemplate.opsForValue().get(orderNo);
//			if(null != payMap) return payMap;

            PaymentInfo paymentInfo = paymentService.getPaymentInfo(orderNo, PaymentType.WEIXIN);
            if(null == paymentInfo) {
                paymentInfo = paymentService.savePaymentInfo(orderNo, PaymentType.WEIXIN);
            }

            Map<String, String> paramMap = new HashMap();
            //1、设置参数
            paramMap.put("appid", ConstantPropertiesUtils.APPID);
            paramMap.put("mch_id", ConstantPropertiesUtils.PARTNER);
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
            paramMap.put("body", paymentInfo.getSubject());
            paramMap.put("out_trade_no", paymentInfo.getOrderNo());
            int totalFee = paymentInfo.getTotalAmount().multiply(new BigDecimal(100)).intValue();
            paramMap.put("total_fee", String.valueOf(totalFee));
            paramMap.put("spbill_create_ip", "127.0.0.1");
            paramMap.put("notify_url", ConstantPropertiesUtils.NOTIFYURL);
            paramMap.put("trade_type", "JSAPI");
//			paramMap.put("openid", "o1R-t5trto9c5sdYt6l1ncGmY5iY");
            UserLoginVo userLoginVo = (UserLoginVo)redisTemplate.opsForValue().get("user:login:" + paymentInfo.getUserId());
            if(null != userLoginVo && !StringUtils.isEmpty(userLoginVo.getOpenId())) {
                paramMap.put("openid", userLoginVo.getOpenId());
            } else {
                paramMap.put("openid", "oD7av4igt-00GI8PqsIlg5FROYnI");
            }

            //2、HTTPClient来根据URL访问第三方接口并且传递参数
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");

            //client设置参数
            client.setXmlParam(WXPayUtil.generateSignedXml(paramMap, ConstantPropertiesUtils.PARTNERKEY));
            client.setHttps(true);
            client.post();
            //3、返回第三方的数据
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            log.info("微信下单返回结果：{}", JSON.toJSONString(resultMap));

            //4、再次封装参数
            Map<String, String> parameterMap = new HashMap<>();
            String prepayId = String.valueOf(resultMap.get("prepay_id"));
            String packages = "prepay_id=" + prepayId;
            parameterMap.put("appId", ConstantPropertiesUtils.APPID);
            parameterMap.put("nonceStr", resultMap.get("nonce_str"));
            parameterMap.put("package", packages);
            parameterMap.put("signType", "MD5");
            parameterMap.put("timeStamp", String.valueOf(new Date().getTime()));
            String sign = WXPayUtil.generateSignature(parameterMap, ConstantPropertiesUtils.PARTNERKEY);

            //返回结果
            Map<String, String> result = new HashMap();
            result.put("timeStamp", parameterMap.get("timeStamp"));
            result.put("nonceStr", parameterMap.get("nonceStr"));
            result.put("signType", "MD5");
            result.put("paySign", sign);
            result.put("package", packages);
            if(null != resultMap.get("result_code")) {
                //微信支付二维码2小时过期，可采取2小时未支付取消订单
                redisTemplate.opsForValue().set(orderNo, result, 120, TimeUnit.MINUTES);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    @Override
    public Map queryPayStatus(String orderNo, String paymentType) {
        try {
            //1、封装参数
            Map paramMap = new HashMap<>();
            paramMap.put("appid", ConstantPropertiesUtils.APPID);
            paramMap.put("mch_id", ConstantPropertiesUtils.PARTNER);
            paramMap.put("out_trade_no", orderNo);
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());

            //2、设置请求
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(paramMap, ConstantPropertiesUtils.PARTNERKEY));
            client.setHttps(true);
            client.post();
            //3、返回第三方的数据
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            //6、转成Map
            //7、返回
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
