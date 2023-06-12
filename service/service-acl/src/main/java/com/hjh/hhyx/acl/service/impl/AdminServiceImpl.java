package com.hjh.hhyx.acl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjh.hhyx.acl.mapper.AdminMapper;
import com.hjh.hhyx.acl.service.AdminService;
import com.hjh.hhyx.acl.service.RoleService;
import com.hjh.hhyx.model.acl.Admin;
import com.hjh.hhyx.vo.acl.AdminQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author 韩
 * @version 1.0
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    @Autowired
    private AdminMapper userMapper;

    @Autowired
    private RoleService roleService;

    @Override
    public IPage<Admin> selectPage(Page<Admin> pageParam, AdminQueryVo userQueryVo) {
        //获取用户名
        String username = userQueryVo.getUsername();
        //获取用户昵称
        String name = userQueryVo.getName();
        //创建条件构造器
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(username)) {
            wrapper.eq(Admin::getUsername, username);
        }
        if (!StringUtils.isEmpty(name)) {
            //封装条件
            wrapper.like(Admin::getName, name);
        }
        //调用mapper方法
        IPage<Admin> pageModel = baseMapper.selectPage(pageParam, wrapper);
        return pageModel;
    }
}
