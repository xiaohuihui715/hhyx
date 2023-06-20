package com.hjh.hhyx.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjh.hhyx.model.product.SkuAttrValue;
import com.hjh.hhyx.model.product.SkuImage;
import com.hjh.hhyx.model.product.SkuInfo;
import com.hjh.hhyx.model.product.SkuPoster;
import com.hjh.hhyx.mq.constant.MQConstant;
import com.hjh.hhyx.mq.service.RabbitService;
import com.hjh.hhyx.product.mapper.SkuInfoMapper;
import com.hjh.hhyx.product.service.SkuAttrValueService;
import com.hjh.hhyx.product.service.SkuImageService;
import com.hjh.hhyx.product.service.SkuInfoService;
import com.hjh.hhyx.product.service.SkuPosterService;
import com.hjh.hhyx.vo.product.SkuInfoQueryVo;
import com.hjh.hhyx.vo.product.SkuInfoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * sku信息 服务实现类
 * </p>
 *
 * @author hjh
 * @since 2023-06-16
 */
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo> implements SkuInfoService {

    @Autowired
    private SkuPosterService skuPosterService;

    @Autowired
    private SkuImageService skuImagesService;

    @Autowired
    private SkuAttrValueService skuAttrValueService;

    @Autowired
    private SkuInfoMapper skuInfoMapper;

    @Autowired
    private RabbitService rabbitService;

    //获取sku分页列表
    @Override
    public IPage<SkuInfo> selectPage(Page<SkuInfo> pageParam, SkuInfoQueryVo skuInfoQueryVo) {
        //获取条件值
        String keyword = skuInfoQueryVo.getKeyword();
        String skuType = skuInfoQueryVo.getSkuType();
        Long categoryId = skuInfoQueryVo.getCategoryId();
        //封装条件
        LambdaQueryWrapper<SkuInfo> wrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(keyword)) {
            wrapper.like(SkuInfo::getSkuName, keyword);
        }
        if (!StringUtils.isEmpty(skuType)) {
            wrapper.eq(SkuInfo::getSkuType, skuType);
        }
        if (!StringUtils.isEmpty(categoryId)) {
            wrapper.eq(SkuInfo::getCategoryId, categoryId);
        }
        //调用方法查询
        IPage<SkuInfo> skuInfoPage = baseMapper.selectPage(pageParam, wrapper);
        return skuInfoPage;
    }


    //添加商品
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void saveSkuInfo(SkuInfoVo skuInfoVo) {
        //保存sku信息
        SkuInfo skuInfo = new SkuInfo();
        BeanUtils.copyProperties(skuInfoVo, skuInfo);
        this.save(skuInfo);

        //保存sku海报
        List<SkuPoster> skuPosterList = skuInfoVo.getSkuPosterList();
        if (!CollectionUtils.isEmpty(skuPosterList)) {
            for (SkuPoster skuPoster : skuPosterList) {
                skuPoster.setSkuId(skuInfo.getId());
            }
            skuPosterService.saveBatch(skuPosterList);
        }

        //保存sku图片
        List<SkuImage> skuImagesList = skuInfoVo.getSkuImagesList();
        if (!CollectionUtils.isEmpty(skuImagesList)) {
            int sort = 1;
            for (SkuImage skuImages : skuImagesList) {
                skuImages.setSkuId(skuInfo.getId());
                skuImages.setSort(sort);
                sort++;
            }
            skuImagesService.saveBatch(skuImagesList);
        }

        //保存sku平台属性
        List<SkuAttrValue> skuAttrValueList = skuInfoVo.getSkuAttrValueList();
        if (!CollectionUtils.isEmpty(skuAttrValueList)) {
            int sort = 1;
            for (SkuAttrValue skuAttrValue : skuAttrValueList) {
                skuAttrValue.setSkuId(skuInfo.getId());
                skuAttrValue.setSort(sort);
                sort++;
            }
            skuAttrValueService.saveBatch(skuAttrValueList);
        }
    }

    //获取商品
    @Override
    public SkuInfoVo getSkuInfoVo(Long skuId) {
        SkuInfoVo skuInfoVo = new SkuInfoVo();

        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        //TODO skuImagesService  skuPosterService  skuAttrValueService分别添加方法
        List<SkuImage> skuImageList = skuImagesService.findBySkuId(skuId);
        List<SkuPoster> skuPosterList = skuPosterService.findBySkuId(skuId);
        List<SkuAttrValue> skuAttrValueList = skuAttrValueService.findBySkuId(skuId);

        BeanUtils.copyProperties(skuInfo, skuInfoVo);
        skuInfoVo.setSkuImagesList(skuImageList);
        skuInfoVo.setSkuPosterList(skuPosterList);
        skuInfoVo.setSkuAttrValueList(skuAttrValueList);
        return skuInfoVo;
    }

    //修改商品
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void updateSkuInfo(SkuInfoVo skuInfoVo) {
        Long id = skuInfoVo.getId();
        SkuInfo skuInfo = new SkuInfo();
        BeanUtils.copyProperties(skuInfoVo, skuInfo);
        //更新sku信息
        baseMapper.updateById(skuInfo);

        //保存sku详情
        skuPosterService.remove(new LambdaQueryWrapper<SkuPoster>().eq(SkuPoster::getSkuId, id));
        //保存sku海报
        List<SkuPoster> skuPosterList = skuInfoVo.getSkuPosterList();
        if (!CollectionUtils.isEmpty(skuPosterList)) {
            int sort = 1;
            for (SkuPoster skuPoster : skuPosterList) {
                skuPoster.setSkuId(id);
                sort++;
            }
            skuPosterService.saveBatch(skuPosterList);
        }

        //删除sku图片
        skuImagesService.remove(new LambdaQueryWrapper<SkuImage>().eq(SkuImage::getSkuId, id));
        //保存sku图片
        List<SkuImage> skuImagesList = skuInfoVo.getSkuImagesList();
        if (!CollectionUtils.isEmpty(skuImagesList)) {
            int sort = 1;
            for (SkuImage skuImages : skuImagesList) {
                skuImages.setSkuId(id);
                skuImages.setSort(sort);
                sort++;
            }
            skuImagesService.saveBatch(skuImagesList);
        }

        //删除sku平台属性
        skuAttrValueService.remove(new LambdaQueryWrapper<SkuAttrValue>().eq(SkuAttrValue::getSkuId, id));
        //保存sku平台属性
        List<SkuAttrValue> skuAttrValueList = skuInfoVo.getSkuAttrValueList();
        if (!CollectionUtils.isEmpty(skuAttrValueList)) {
            int sort = 1;
            for (SkuAttrValue skuAttrValue : skuAttrValueList) {
                skuAttrValue.setSkuId(id);
                skuAttrValue.setSort(sort);
                sort++;
            }
            skuAttrValueService.saveBatch(skuAttrValueList);
        }
    }

    //商品审核
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void check(Long skuId, Integer status) {
        // 更改发布状态
        SkuInfo skuInfoUp = new SkuInfo();
        skuInfoUp.setId(skuId);
        skuInfoUp.setCheckStatus(status);
        baseMapper.updateById(skuInfoUp);
    }

    //商品上架
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void publish(Long skuId, Integer status) {
        // 更改发布状态
        if (status == 1) {
            SkuInfo skuInfoUp = new SkuInfo();
            skuInfoUp.setId(skuId);
            skuInfoUp.setPublishStatus(1);
            skuInfoMapper.updateById(skuInfoUp);

            //TODO 商品上架 后续会完善：发送mq消息更新es数据
            //商品上架：发送mq消息同步es
            rabbitService.sendMessage(MQConstant.EXCHANGE_GOODS_DIRECT, MQConstant.ROUTING_GOODS_UPPER, skuId);
        } else {
            SkuInfo skuInfoUp = new SkuInfo();
            skuInfoUp.setId(skuId);
            skuInfoUp.setPublishStatus(0);
            skuInfoMapper.updateById(skuInfoUp);

            //TODO 商品下架 后续会完善：发送mq消息更新es数据
            //商品下架：发送mq消息同步es
            rabbitService.sendMessage(MQConstant.EXCHANGE_GOODS_DIRECT, MQConstant.ROUTING_GOODS_LOWER, skuId);
        }
    }

    //新人专享
    @Override
    public void isNewPerson(Long skuId, Integer status) {
        SkuInfo skuInfoUp = new SkuInfo();
        skuInfoUp.setId(skuId);
        skuInfoUp.setIsNewPerson(status);
        skuInfoMapper.updateById(skuInfoUp);
    }

    //批量获取sku信息
    @Override
    public List<SkuInfo> findSkuInfoList(List<Long> skuIdList) {
//        return null;
        List<SkuInfo> list = new ArrayList<>();
        for (Long id : skuIdList) {
            list.add(skuInfoMapper.selectOne(new LambdaQueryWrapper<SkuInfo>().eq(SkuInfo::getId, id)));
        }
        return list;
    }

    //根据关键字获取sku列表
    @Override
    public List<SkuInfo> findSkuInfoByKeyword(String keyword) {
        LambdaQueryWrapper<SkuInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(SkuInfo::getSkuName, keyword);
        return baseMapper.selectList(queryWrapper);
    }

}
