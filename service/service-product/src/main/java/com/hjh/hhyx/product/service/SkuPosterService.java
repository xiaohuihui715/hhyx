package com.hjh.hhyx.product.service;

import com.hjh.hhyx.model.product.SkuPoster;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品海报表 服务类
 * </p>
 *
 * @author hjh
 * @since 2023-06-16
 */
public interface SkuPosterService extends IService<SkuPoster> {

    //根据skuid获取sku海报
    List<SkuPoster> findBySkuId(Long skuId);
}
