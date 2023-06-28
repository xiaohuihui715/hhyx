package com.hjh.hhyx.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hjh.hhyx.model.order.OrderInfo;
import com.hjh.hhyx.vo.order.OrderConfirmVo;
import com.hjh.hhyx.vo.order.OrderSubmitVo;
import com.hjh.hhyx.vo.order.OrderUserQueryVo;

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


    OrderInfo getOrderInfoByOrderNo(String orderNo);

    //更新订单支付状态
    void orderPay(String orderNo);


    //查询用户订单页面
    IPage<OrderInfo> getOrderInfoByUserIdPage(Page<OrderInfo> pageParam, OrderUserQueryVo orderUserQueryVo);
}
