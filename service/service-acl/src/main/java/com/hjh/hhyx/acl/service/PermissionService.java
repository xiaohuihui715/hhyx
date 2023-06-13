package com.hjh.hhyx.acl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hjh.hhyx.model.acl.Permission;

import java.util.List;

/**
 * @author 韩
 * @version 1.0
 */
public interface PermissionService extends IService<Permission> {
    /**
     * 查询所有菜单
     * @return
     */
    List<Permission> queryAllMenu();

    /**
     * 移除某个菜单下所有子菜单
     * @param id
     */
    boolean removeChildById(Long id);

    /**
     * 获取该角色的权限列表
     * @param roleId
     * @return
     */
    List<Permission> findMenuByRoleId(Long roleId);

    /**
     * 为用户分配菜单权限
     * @param roleId
     * @param permissionId
     * @return
     */
    boolean assignPerssionsByRoleId(Long roleId, Long[] permissionId);
}

