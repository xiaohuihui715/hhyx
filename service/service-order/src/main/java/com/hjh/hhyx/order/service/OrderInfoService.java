package com.hjh.hhyx.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hjh.hhyx.model.order.OrderInfo;
import com.hjh.hhyx.vo.order.OrderConfirmVo;
import com.hjh.hhyx.vo.order.OrderSubmitVo;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author hjh
 * @since 2023-06-24
 */
public interface OrderInfoService extends IService<OrderInfo> {

    /**
     * 确认订单
     */
    OrderConfirmVo confirmOrder();

    //生成订单
    Long submitOrder(OrderSubmitVo orderParamVo);

    //订单详情
    OrderInfo getOrderInfoById(Long orderId);


}
