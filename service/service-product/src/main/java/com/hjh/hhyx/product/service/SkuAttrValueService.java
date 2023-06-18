package com.hjh.hhyx.product.service;

import com.hjh.hhyx.model.product.SkuAttrValue;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * spu属性值 服务类
 * </p>
 *
 * @author hjh
 * @since 2023-06-16
 */
public interface SkuAttrValueService extends IService<SkuAttrValue> {

    //根据skuid获取sku平台属性
    List<SkuAttrValue> findBySkuId(Long skuId);
}
