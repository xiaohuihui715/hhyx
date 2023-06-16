package com.hjh.hhyx.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hjh.hhyx.model.sys.RegionWare;
import com.hjh.hhyx.vo.sys.RegionWareQueryVo;

/**
 * <p>
 * 城市仓库关联表 服务类
 * </p>
 *
 * @author hjh
 * @since 2023-06-15
 */
public interface RegionWareService extends IService<RegionWare> {

    //查询区域列表
    IPage<RegionWare> selectPage(Page<RegionWare> pageParam,
                                 RegionWareQueryVo regionWareQueryVo);

    //添加开通区域
    void saveRegionWare(RegionWare regionWare);

    //取消开通区域
    void updateStatus(Long id, Integer status);

}
