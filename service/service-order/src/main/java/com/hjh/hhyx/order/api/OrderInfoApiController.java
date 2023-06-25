package com.hjh.hhyx.order.api;

import com.hjh.hhyx.model.order.OrderInfo;
import com.hjh.hhyx.order.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 韩
 * @version 1.0
 */
@RestController
@RequestMapping(value="/api/order")
public class OrderInfoApiController {

    @Autowired
    private OrderInfoService orderInfoService;

    /**
     * 根据orderNo查询订单信息
     * @param orderNo
     * @return
     */
    @GetMapping("inner/getOrderInfo/{orderNo}")
    public OrderInfo getOrderInfo(@PathVariable("orderNo") String orderNo){
        OrderInfo orderInfo = orderInfoService.getOrderInfoByOrderNo(orderNo);
        return orderInfo;
    }
}
