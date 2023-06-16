package com.hjh.hhyx.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hjh.hhyx.model.sys.Region;

import java.util.List;

/**
 * <p>
 * 地区表 服务类
 * </p>
 *
 * @author hjh
 * @since 2023-06-15
 */
public interface RegionService extends IService<Region> {

    //根据关键字获取地区列表
    List<Region> findRegionByKeyword(String keyword);
}
