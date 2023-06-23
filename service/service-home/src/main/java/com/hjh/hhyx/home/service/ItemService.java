package com.hjh.hhyx.home.service;

import java.util.Map;

/**
 * @author 韩
 * @version 1.0
 */
public interface ItemService {

    //获取sku详细信息
    Map<String, Object> item(Long skuId, Long userId);
}