package com.hjh.hhyx.acl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hjh.hhyx.model.acl.Admin;
import com.hjh.hhyx.vo.acl.AdminQueryVo;

/**
 * @author 韩
 * @version 1.0
 */
public interface AdminService extends IService<Admin> {
    /**
     * 获取用户分页列表
     * @param pageParam
     * @param userQueryVo：用户名，昵称模糊匹配查询
     * @return
     */
    IPage<Admin> selectPage(Page<Admin> pageParam, AdminQueryVo userQueryVo);
}
