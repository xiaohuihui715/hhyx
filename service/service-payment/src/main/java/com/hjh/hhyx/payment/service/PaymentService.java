package com.hjh.hhyx.payment.service;

import com.hjh.hhyx.enums.PaymentType;
import com.hjh.hhyx.model.order.PaymentInfo;

import java.util.Map;

/**
 * @author 韩
 * @version 1.0
 */
public interface PaymentService {

    /**
     * 保存交易记录
     * @param orderNo
     * @param paymentType 支付类型（1：微信 2：支付宝）
     */
    PaymentInfo savePaymentInfo(String orderNo, PaymentType paymentType);

    PaymentInfo getPaymentInfo(String orderNo, PaymentType paymentType);

    //支付成功
    void paySuccess(String orderNo,PaymentType paymentType, Map<String,String> paramMap);
}
