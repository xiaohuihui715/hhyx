package com.hjh.hhyx.acl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjh.hhyx.acl.mapper.PermissionMapper;
import com.hjh.hhyx.acl.service.PermissionService;
import com.hjh.hhyx.acl.service.RolePermissionService;
import com.hjh.hhyx.acl.utils.PermissionHelper;
import com.hjh.hhyx.model.acl.Permission;
import com.hjh.hhyx.model.acl.RolePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 韩
 * @version 1.0
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private RolePermissionService rolePermissionService;

    //获取所有菜单
    @Override
    public List<Permission> queryAllMenu() {
        //获取全部权限数据
        List<Permission> allPermissionList = baseMapper.selectList(new QueryWrapper<Permission>().orderByAsc("CAST(id AS SIGNED)"));

        //把权限数据构建成树形结构数据,树形结构显示数据
        List<Permission> result = PermissionHelper.bulid(allPermissionList);
        return result;
    }


    //递归删除菜单
    @Override
    public boolean removeChildById(Long id) {
        List<Long> idList = new ArrayList<>();
        this.selectChildListById(id, idList);
        idList.add(id);
        baseMapper.deleteBatchIds(idList);
        return true;
    }

    @Override
    public List<Permission> findMenuByRoleId(Long roleId) {
        //获取所有菜单权限
        List<Permission> permissionList = baseMapper.selectList(null);

        LambdaQueryWrapper<RolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RolePermission::getRoleId, roleId);
        List<RolePermission> rolePermissionList = rolePermissionService.list(wrapper);

        List<Long> permissionIds = rolePermissionList.stream().map(item -> item.getPermissionId()).collect(Collectors.toList());
        List<Permission> assginPermissions = new ArrayList<>();
        for (Permission permission : permissionList) {
            if (permissionIds.contains(permission.getId())) {
                permission.setSelect(true);//对该用户已分配的权限设置标识
            }
        }
        //构造树形结构的权限列表
        List<Permission> allPermissions = PermissionHelper.bulid(permissionList);
        return allPermissions;
    }

    @Override
    public boolean assignPerssionsByRoleId(Long roleId, Long[] permissionId) {
        //移除已分配的用户菜单权限
        LambdaQueryWrapper<RolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RolePermission::getRoleId, roleId);
        rolePermissionService.remove(wrapper);

        ArrayList<RolePermission> rolePermissions = new ArrayList<>();
        for (Long aLong : permissionId) {
            if (StringUtils.isEmpty(aLong)) continue;
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(aLong);
            rolePermissions.add(rolePermission);
        }
        //批量添加角色菜单权限表
        rolePermissionService.saveBatch(rolePermissions);
        return false;
    }

    /**
     * 递归获取子节点列表
     *
     * @param id
     * @param idList
     */
    private void selectChildListById(Long id, List<Long> idList) {
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Permission::getPid, id);
        List<Permission> childList = baseMapper.selectList(wrapper);
        childList.stream().forEach(item -> {
            idList.add(item.getId());
            this.selectChildListById(item.getId(), idList);
        });
    }
}
