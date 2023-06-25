package com.hjh.hhyx.cart.service;

import com.hjh.hhyx.model.order.CartInfo;

import java.util.List;

/**
 * @author 韩
 * @version 1.0
 */
public interface CartInfoService {

    // 添加购物车 用户Id，商品Id，商品数量。
    void addToCart(Long skuId, Long userId, Integer skuNum);

    void deleteCart(Long skuId, Long userId);

    /**
     * 批量删除购物车
     * @param userId
     */
    void deleteAllCart(Long userId);

    void batchDeleteCart(List<Long> skuIdList, Long userId);

    /**
     * 通过用户Id 查询购物车列表
     * @param userId
     * @return
     */
    List<CartInfo> getCartList(Long userId);

    /**
     * 更新选中状态
     *
     * @param userId
     * @param isChecked
     * @param skuId
     */
    void checkCart(Long userId, Integer isChecked, Long skuId);

    void checkAllCart(Long userId, Integer isChecked);

    void batchCheckCart(List<Long> skuIdList, Long userId, Integer isChecked);

    //获取购物车选中列表
    List<CartInfo> getCartCheckedList(Long userId);

    //根据用户id删除选中的购物车
    void deleteCartChecked(Long userId);
}
