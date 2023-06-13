package com.hjh.hhyx.acl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjh.hhyx.acl.mapper.RolePermissionMapper;
import com.hjh.hhyx.acl.service.RolePermissionService;
import com.hjh.hhyx.model.acl.RolePermission;
import org.springframework.stereotype.Service;

/**
 * @author 韩
 * @version 1.0
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {
}
