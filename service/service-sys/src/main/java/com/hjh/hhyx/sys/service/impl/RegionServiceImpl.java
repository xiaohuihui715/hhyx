package com.hjh.hhyx.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjh.hhyx.model.sys.Region;
import com.hjh.hhyx.sys.mapper.RegionMapper;
import com.hjh.hhyx.sys.service.RegionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 地区表 服务实现类
 * </p>
 *
 * @author hjh
 * @since 2023-06-15
 */
@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements RegionService {

    //根据关键字获取地区列表
    @Override
    public List<Region> findRegionByKeyword(String keyword) {
        LambdaQueryWrapper<Region> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Region::getName, keyword);
        return baseMapper.selectList(queryWrapper);
    }
}
