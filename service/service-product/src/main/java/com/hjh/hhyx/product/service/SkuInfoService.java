package com.hjh.hhyx.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hjh.hhyx.model.product.SkuInfo;
import com.hjh.hhyx.vo.product.SkuInfoQueryVo;
import com.hjh.hhyx.vo.product.SkuInfoVo;

/**
 * <p>
 * sku信息 服务类
 * </p>
 *
 * @author hjh
 * @since 2023-06-16
 */
public interface SkuInfoService extends IService<SkuInfo> {

    //获取sku分页列表
    IPage<SkuInfo> selectPage(Page<SkuInfo> pageParam, SkuInfoQueryVo skuInfoQueryVo);

    //添加商品
    void saveSkuInfo(SkuInfoVo skuInfoVo);

    //获取商品
    SkuInfoVo getSkuInfoVo(Long id);

    //修改商品
    void updateSkuInfo(SkuInfoVo skuInfoVo);

    //更新商品审核状态
    void check(Long skuId, Integer status);

    //上下架商品
    void publish(Long skuId, Integer status);

    //新人专享
    void isNewPerson(Long skuId, Integer status);
}
