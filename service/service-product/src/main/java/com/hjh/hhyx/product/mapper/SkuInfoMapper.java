package com.hjh.hhyx.product.mapper;

import com.hjh.hhyx.model.product.SkuInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * sku信息 Mapper 接口
 * </p>
 *
 * @author hjh
 * @since 2023-06-16
 */
@Repository
public interface SkuInfoMapper extends BaseMapper<SkuInfo> {

    /**
     * 解锁库存
     *
     * @param skuId
     * @param skuNum
     */
    void unlockStock(@Param("skuId") Long skuId, @Param("skuNum") Integer skuNum);

    /**
     * 查询库存
     *
     * @param skuId
     * @param skuNum
     * @return
     */
    SkuInfo checkStock(@Param("skuId") Long skuId, @Param("skuNum") Integer skuNum);

    /**
     * 锁定库存
     *
     * @param skuId
     * @param skuNum
     * @return
     */
    Integer lockStock(@Param("skuId") Long skuId, @Param("skuNum") Integer skuNum);

    /**
     * 扣减库存
     * @param skuId
     * @param skuNum
     * @return
     */
    Integer minusStock(@Param("skuId")Long skuId, @Param("skuNum")Integer skuNum);
}
