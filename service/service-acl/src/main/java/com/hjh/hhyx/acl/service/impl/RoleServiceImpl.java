package com.hjh.hhyx.acl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjh.hhyx.acl.mapper.RoleMapper;
import com.hjh.hhyx.acl.service.AdminRoleService;
import com.hjh.hhyx.acl.service.RoleService;
import com.hjh.hhyx.model.acl.AdminRole;
import com.hjh.hhyx.model.acl.Role;
import com.hjh.hhyx.vo.acl.RoleQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 韩
 * @version 1.0
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private AdminRoleService adminRoleService;

    @Override
    public IPage<Role> selectPage(Page<Role> pageParam, RoleQueryVo roleQueryVo) {
        //获取条件值：角色名称
        String roleName = roleQueryVo.getRoleName();
        //创建条件构造器对象
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        //判断条件值是否为空
        if (!StringUtils.isEmpty(roleName)) {
            //封装条件
            wrapper.like(Role::getRoleName, roleName);
        }
        //调用mapper方法实现条件分页查询
        IPage<Role> pageModel = baseMapper.selectPage(pageParam, wrapper);
        return pageModel;
    }

    /**
     * 根据用户id查询用户已分配的角色和所有角色列表
     *
     * @param adminId
     * @return ： 两个list，一个存放所有角色列表，一个存放该用户已分配的角色列表
     */
    @Override
    public Map<String, Object> findRoleByUserId(Long adminId) {
        //1.查询所有的角色
        List<Role> allRolesList = baseMapper.selectList(null);

        //2.根据用户id查询已分配的角色列表
        //2.1根据用户id查询已分配的AdminRole的角色信息列表
        LambdaQueryWrapper<AdminRole> adminRoleQueryWrapper = new LambdaQueryWrapper<>();
        adminRoleQueryWrapper.eq(AdminRole::getAdminId, adminId);
        //根据上述条件查询
        List<AdminRole> adminRoleList = adminRoleService.list(adminRoleQueryWrapper);
        //2.2将上述AdminRole列表中的角色id封装到list集合中
        List<Long> roleIdList = adminRoleList.stream().map(item -> item.getRoleId()).collect(Collectors.toList());

        //2.3 根据所有角色列表和已分配角色id列表查询已分配的角色信息列表
        List<Role> assignRoleList = new ArrayList<>();
        for (Role role : allRolesList) {
            if (roleIdList.contains(role.getId())) {
                assignRoleList.add(role);
            }
        }

        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("assignRoles", assignRoleList);//该用户已分配的角色信息列表
        roleMap.put("allRolesList", allRolesList);//所有角色列表
        return roleMap;
    }

    /**
     * 为用户分配角色
     *
     * @param adminId
     * @param roleIds：角色id列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUserRoleRealtionShip(Long adminId, Long[] roleIds) {
        //删除用户分配的角色数据
        adminRoleService.remove(new LambdaQueryWrapper<AdminRole>().eq(AdminRole::getAdminId, adminId));

        //分配新的角色
        List<AdminRole> userRoleList = new ArrayList<>();
        for (Long roleId : roleIds) {
            if (StringUtils.isEmpty(roleId)) continue;
            AdminRole userRole = new AdminRole();
            userRole.setAdminId(adminId);
            userRole.setRoleId(roleId);
            userRoleList.add(userRole);
        }
        adminRoleService.saveBatch(userRoleList);
    }
}
