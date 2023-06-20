package com.hjh.hhyx.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hjh.hhyx.model.product.Category;
import com.hjh.hhyx.vo.product.CategoryQueryVo;

import java.util.List;

/**
 * <p>
 * 商品三级分类 服务类
 * </p>
 *
 * @author hjh
 * @since 2023-06-16
 */
public interface CategoryService extends IService<Category> {

    //商品分类分页列表
    IPage<Category> selectPage(Page<Category> pageParam, CategoryQueryVo categoryQueryVo);

    //查询所有商品分类
    List<Category> findAllList();

    List<Category> findCategoryList(List<Long> categoryIdList);
}
