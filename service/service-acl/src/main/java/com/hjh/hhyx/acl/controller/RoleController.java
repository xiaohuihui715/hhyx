package com.hjh.hhyx.acl.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hjh.hhyx.acl.service.RoleService;
import com.hjh.hhyx.common.result.Result;
import com.hjh.hhyx.model.acl.Role;
import com.hjh.hhyx.vo.acl.RoleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 韩
 * @version 1.0
 */
@Api(tags = "角色接口")
@RestController
@RequestMapping("/admin/acl/role")
@CrossOrigin//跨域
public class RoleController {
    @Autowired
    private RoleService roleService;


    /**
     * 获取角色分页列表，包括模糊匹配
     *
     * @param page
     * @param limit
     * @param roleQueryVo ： 角色名模糊查询
     * @return
     */
    @ApiOperation(value = "获取角色分页列表")
    @GetMapping("{page}/{limit}")
    public Result index(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "roleQueryVo", value = "查询对象", required = false)
            RoleQueryVo roleQueryVo) {
        Page<Role> pageParam = new Page<>(page, limit);
        IPage<Role> pageModel = roleService.selectPage(pageParam, roleQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "获取角色")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        Role role = roleService.getById(id);
        return Result.ok(role);
    }

    @ApiOperation(value = "新增角色")
    @PostMapping("save")
    public Result save(@RequestBody Role role/*前端通过json数据传过来的*/) {
        boolean is_save = roleService.save(role);
        if (is_save) {
            return Result.ok(null);
        } else {
            return Result.fail(null);
        }
    }

    @ApiOperation(value = "修改角色")
    @PutMapping("update")
    public Result updateById(@RequestBody Role role) {
        roleService.updateById(role);
        return Result.ok(null);
    }

    @ApiOperation(value = "根据id删除角色")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        roleService.removeById(id);
        return Result.ok(null);
    }

    @ApiOperation(value = "根据id列表批量删除角色")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        roleService.removeByIds(idList);
        return Result.ok(null);
    }




}
