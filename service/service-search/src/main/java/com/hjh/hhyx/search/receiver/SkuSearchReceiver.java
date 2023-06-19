package com.hjh.hhyx.search.receiver;

import com.hjh.hhyx.mq.constant.MQConstant;
import com.hjh.hhyx.search.service.SkuSearchService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author 韩
 * @version 1.0
 */
@Component
public class SkuSearchReceiver {

    @Autowired
    private SkuSearchService skuSearchService;

    /**
     * 商品上架
     *
     * @param skuId
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MQConstant.QUEUE_GOODS_UPPER, durable = "true"),
            exchange = @Exchange(value = MQConstant.EXCHANGE_GOODS_DIRECT),
            key = {MQConstant.ROUTING_GOODS_UPPER}
    ))
    public void upperSku(Long skuId, Message message, Channel channel) throws IOException {
        if (null != skuId) {
            skuSearchService.upperSku(skuId);
        }
        /**
         * 第一个参数：表示收到的消息的标号
         * 第二个参数：如果为true表示可以签收多个消息
         */
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    /**
     * 商品下架
     *
     * @param skuId
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MQConstant.QUEUE_GOODS_LOWER, durable = "true"),
            exchange = @Exchange(value = MQConstant.EXCHANGE_GOODS_DIRECT),
            key = {MQConstant.ROUTING_GOODS_LOWER}
    ))
    public void lowerSku(Long skuId, Message message, Channel channel) throws IOException {
        if (null != skuId) {
            skuSearchService.lowerSku(skuId);
        }
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
