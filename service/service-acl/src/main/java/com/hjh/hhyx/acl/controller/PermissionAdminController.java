package com.hjh.hhyx.acl.controller;

import com.hjh.hhyx.acl.service.PermissionService;
import com.hjh.hhyx.common.result.Result;
import com.hjh.hhyx.model.acl.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 韩
 * @version 1.0
 * 菜单管理
 */
@RestController
@RequestMapping("/admin/acl/permission")
@Api(tags = "菜单管理")
@CrossOrigin //跨域
public class PermissionAdminController {

    @Autowired
    private PermissionService permissionService;

    @ApiOperation(value = "获取菜单")
    @GetMapping
    public Result index() {
        List<Permission> list = permissionService.queryAllMenu();
        return Result.ok(list);
    }

    @ApiOperation(value = "新增菜单")
    @PostMapping("save")
    public Result save(@RequestBody Permission permission) {
        permissionService.save(permission);
        return Result.ok(null);
    }

    @ApiOperation(value = "修改菜单")
    @PutMapping("update")
    public Result updateById(@RequestBody Permission permission) {
        permissionService.updateById(permission);
        return Result.ok(null);
    }

    @ApiOperation(value = "递归删除菜单")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        permissionService.removeChildById(id);
        return Result.ok(null);
    }


    @ApiOperation(value = "根据角色id获取角色的分配菜单权限")
    @GetMapping("toAssign/{roleId}")
    public Result getAssign(@PathVariable Long roleId) {
        List<Permission> allPermissions = permissionService.findMenuByRoleId(roleId);
        return Result.ok(allPermissions);
    }

    @ApiOperation(value = "根据角色id分配菜单权限")
    @PostMapping("doAssign")
    public Result doAssign(@RequestParam Long roleId, Long[] permissionId) {
        permissionService.assignPerssionsByRoleId(roleId, permissionId);
        return Result.ok(null);
    }
}