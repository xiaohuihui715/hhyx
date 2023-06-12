package com.hjh.hhyx.acl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjh.hhyx.acl.mapper.AdminRoleMapper;
import com.hjh.hhyx.acl.service.AdminRoleService;
import com.hjh.hhyx.model.acl.AdminRole;
import org.springframework.stereotype.Service;

/**
 * @author 韩
 * @version 1.0
 */
@Service
public class AdminRoleServiceImpl extends ServiceImpl<AdminRoleMapper, AdminRole> implements AdminRoleService {
}
