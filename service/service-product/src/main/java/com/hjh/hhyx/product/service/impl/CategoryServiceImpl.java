package com.hjh.hhyx.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjh.hhyx.model.product.Category;
import com.hjh.hhyx.product.mapper.CategoryMapper;
import com.hjh.hhyx.product.service.CategoryService;
import com.hjh.hhyx.vo.product.CategoryQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 商品三级分类 服务实现类
 * </p>
 *
 * @author hjh
 * @since 2023-06-16
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    //商品分类分页列表
    @Override
    public IPage<Category> selectPage(Page<Category> pageParam, CategoryQueryVo categoryQueryVo) {
        String name = categoryQueryVo.getName();
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like(Category::getName, name);
        }
        IPage<Category> categoryPage = baseMapper.selectPage(pageParam, queryWrapper);
        return categoryPage;
    }

    //查询所有商品分类
    @Override
    public List<Category> findAllList() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        return this.list(queryWrapper);
    }

    @Override
    public List<Category> findCategoryList(List<Long> categoryIdList) {
        List<Category> list = new ArrayList<>();
        for (Long id : categoryIdList) {
            list.add(baseMapper.selectOne(new LambdaQueryWrapper<Category>().eq(Category::getId, id)));
        }
        return list;
    }
}
