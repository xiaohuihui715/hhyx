package com.hjh.hhyx.cart.receiver;

import com.hjh.hhyx.cart.service.CartInfoService;
import com.hjh.hhyx.mq.constant.MQConstant;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author 韩
 * @version 1.0
 */
@Component
public class CartReceiver {

    @Resource
    private CartInfoService cartInfoService;


    /**
     * 删除购物车选项
     *
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MQConstant.QUEUE_DELETE_CART, durable = "true"),
            exchange = @Exchange(value = MQConstant.EXCHANGE_ORDER_DIRECT),
            key = {MQConstant.ROUTING_DELETE_CART}
    ))
    public void deleteCart(Long userId, Message message, Channel channel) throws IOException {
        if (null != userId) {
            // 获取用户id和skuIdList
            cartInfoService.deleteCartChecked(userId);
        }
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
