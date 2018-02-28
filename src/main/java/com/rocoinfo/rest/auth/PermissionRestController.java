package com.rocoinfo.rest.auth;

import com.rocoinfo.aop.logger.Logger;
import com.rocoinfo.dto.StatusDto;
import com.rocoinfo.dto.app.PermissionTreeDto;
import com.rocoinfo.entity.auth.Permission;
import com.rocoinfo.service.auth.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <dl>
 * <dd>Description: 功能描述</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/5/31 17:45</dd>
 * <dd>@author：Kong</dd>
 * </dl>
 */
@RestController(value = "appPermissionRestController")
@RequestMapping("/api/app/permissions")
public class PermissionRestController {

    @Autowired
    private PermissionService permissionService;

    /**
     * @return
     */
    @GetMapping
    @Logger(module = com.rocoinfo.config.log.Type.APP_PERMISSION)
    public StatusDto search(@RequestParam(value = "appId") Long appId) {
        PermissionTreeDto permissionTreeDto = permissionService.findPermissionTree(appId);
        if (permissionTreeDto != null) {
            return StatusDto.buildSuccess(permissionTreeDto);
        }
        return StatusDto.buildFailure("没有查询该应用的权限信息,请联系管理员添加该应用权限根节点!");

    }


    /**
     * 查询应用角色详情
     *
     * @param id 权限id
     * @return 返回权限信息
     */
    @GetMapping("/{id}/get")
    @Logger(module = com.rocoinfo.config.log.Type.APP_PERMISSION)
    public StatusDto getById(@PathVariable(value = "id") Long id) {
        return StatusDto.buildSuccess(permissionService.getById(id));
    }

    /**
     * 删除角色
     *
     * @param id 角色id
     * @return 返回删除结果
     */
    @DeleteMapping("/{id}/del")
    @Logger(module = com.rocoinfo.config.log.Type.APP_PERMISSION)
    public StatusDto delete(@PathVariable(value = "id") Long id) {
        permissionService.deleteById(id);
        return StatusDto.buildSuccess("删除成功！");
    }

    /**
     * 新建或更新应用角色
     *
     * @param permission 角色信息
     * @return 返回更新结果
     */
    @PostMapping
    @Logger(module = com.rocoinfo.config.log.Type.APP_PERMISSION)
    public StatusDto createOrUpdate(@RequestBody @Validated Permission permission) {
        PermissionTreeDto treeDto = permissionService.createOrUpdate(permission);
        return StatusDto.buildSuccess("操作成功",treeDto);
    }
}
