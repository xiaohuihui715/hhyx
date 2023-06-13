package com.hjh.hhyx.acl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hjh.hhyx.model.acl.Role;
import com.hjh.hhyx.vo.acl.RoleQueryVo;

import java.util.Map;

/**
 * @author 韩
 * @version 1.0
 */
public interface RoleService extends IService<Role> {
    /**
     * 查询某页的角色列表
     * @param pageParam
     * @param roleQueryVo
     * @return
     */
    IPage<Role> selectPage(Page<Role> pageParam, RoleQueryVo roleQueryVo);

    /**
     * 根绝用户id获取已分配的角色列表和所有角色列表
     * @param adminId
     * @return
     */
    Map<String, Object> findRoleByUserId(Long adminId);

    /**
     * 为用户分配角色
     * @param adminId
     * @param roleId：角色id列表
     */
    void saveUserRoleRealtionShip(Long adminId, Long[] roleId);



}
