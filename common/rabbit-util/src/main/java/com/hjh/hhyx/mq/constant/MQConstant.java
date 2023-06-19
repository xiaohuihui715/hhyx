package com.hjh.hhyx.mq.constant;

/**
 * @author 韩
 * @version 1.0
 * mq中使用的常量
 */
public class MQConstant {

    /**
     * 消息补偿
     */
    public static final String MQ_KEY_PREFIX = "hhyx.mq:list";
    public static final int RETRY_COUNT = 3;

    /**
     * 商品上下架
     */
    public static final String EXCHANGE_GOODS_DIRECT = "hhyx.goods.direct";
    public static final String ROUTING_GOODS_UPPER = "hhyx.goods.upper";
    public static final String ROUTING_GOODS_LOWER = "hhyx.goods.lower";
    //队列
    public static final String QUEUE_GOODS_UPPER  = "hhyx.goods.upper";
    public static final String QUEUE_GOODS_LOWER  = "hhyx.goods.lower";

    /**
     * 团长上下线
     */
    public static final String EXCHANGE_LEADER_DIRECT = "hhyx.leader.direct";
    public static final String ROUTING_LEADER_UPPER = "hhyx.leader.upper";
    public static final String ROUTING_LEADER_LOWER = "hhyx.leader.lower";
    //队列
    public static final String QUEUE_LEADER_UPPER  = "hhyx.leader.upper";
    public static final String QUEUE_LEADER_LOWER  = "hhyx.leader.lower";

    //订单
    public static final String EXCHANGE_ORDER_DIRECT = "hhyx.order.direct";
    public static final String ROUTING_ROLLBACK_STOCK = "hhyx.rollback.stock";
    public static final String ROUTING_MINUS_STOCK = "hhyx.minus.stock";

    public static final String ROUTING_DELETE_CART = "hhyx.delete.cart";
    //解锁普通商品库存
    public static final String QUEUE_ROLLBACK_STOCK = "hhyx.rollback.stock";
    public static final String QUEUE_SECKILL_ROLLBACK_STOCK = "hhyx.seckill.rollback.stock";
    public static final String QUEUE_MINUS_STOCK = "hhyx.minus.stock";
    public static final String QUEUE_DELETE_CART = "hhyx.delete.cart";

    //支付
    public static final String EXCHANGE_PAY_DIRECT = "hhyx.pay.direct";
    public static final String ROUTING_PAY_SUCCESS = "hhyx.pay.success";
    public static final String QUEUE_ORDER_PAY  = "hhyx.order.pay";
    public static final String QUEUE_LEADER_BILL  = "hhyx.leader.bill";

    //取消订单
    public static final String EXCHANGE_CANCEL_ORDER_DIRECT = "hhyx.cancel.order.direct";
    public static final String ROUTING_CANCEL_ORDER = "hhyx.cancel.order";
    //延迟取消订单队列
    public static final String QUEUE_CANCEL_ORDER  = "hhyx.cancel.order";

    /**
     * 定时任务
     */
    public static final String EXCHANGE_DIRECT_TASK = "hhyx.exchange.direct.task";
    public static final String ROUTING_TASK_23 = "hhyx.task.23";
    //队列
    public static final String QUEUE_TASK_23  = "hhyx.queue.task.23";
}
