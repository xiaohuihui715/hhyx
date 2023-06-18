package com.hjh.hhyx.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hjh.hhyx.model.product.SkuAttrValue;
import com.hjh.hhyx.product.mapper.SkuAttrValueMapper;
import com.hjh.hhyx.product.service.SkuAttrValueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * spu属性值 服务实现类
 * </p>
 *
 * @author hjh
 * @since 2023-06-16
 */
@Service
public class SkuAttrValueServiceImpl extends ServiceImpl<SkuAttrValueMapper, SkuAttrValue> implements SkuAttrValueService {

    @Override
    public List<SkuAttrValue> findBySkuId(Long skuId) {
        LambdaQueryWrapper<SkuAttrValue> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SkuAttrValue::getSkuId, skuId);
        List<SkuAttrValue> list = baseMapper.selectList(wrapper);
        return list;
    }
}
