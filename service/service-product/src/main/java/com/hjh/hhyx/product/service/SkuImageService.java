package com.hjh.hhyx.product.service;

import com.hjh.hhyx.model.product.SkuImage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品图片 服务类
 * </p>
 *
 * @author hjh
 * @since 2023-06-16
 */
public interface SkuImageService extends IService<SkuImage> {

    //根据skuId查询Sku图片
    List<SkuImage> findBySkuId(Long skuId);
}
