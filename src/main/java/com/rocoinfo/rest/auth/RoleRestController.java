package com.rocoinfo.rest.auth;

import com.rocoinfo.aop.logger.Logger;
import com.rocoinfo.common.BaseController;
import com.rocoinfo.dto.StatusDto;
import com.rocoinfo.dto.page.PageTable;
import com.rocoinfo.dto.page.Pagination;
import com.rocoinfo.dto.page.Sort;
import com.rocoinfo.entity.auth.Role;
import com.rocoinfo.repository.auth.RoleDao;
import com.rocoinfo.service.auth.RoleService;
import com.rocoinfo.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <dl>
 * <dd>Description: 功能描述</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/5/31 17:44</dd>
 * <dd>@author：Kong</dd>
 * </dl>
 */
@RestController(value = "appRoleRestController")
@RequestMapping("/api/app/roles")
public class RoleRestController extends BaseController {

    @Autowired
    private RoleService roleService;

    /**
     * @param keyword   查询关键字
     * @param appId     应用id
     * @param offset    查询起点
     * @param limit     查询条数
     * @param sortName  排序字段
     * @param sortOrder 排序方式
     * @return 返回角色列表
     */
    @GetMapping
    @Logger(module = com.rocoinfo.config.log.Type.APP_ROLE)
    public StatusDto search(@RequestParam(name = "keyword", required = false) String keyword,
                            @RequestParam(name = "appId") Long appId,
                            @RequestParam(name = "offset", defaultValue = "0") int offset,
                            @RequestParam(name = "limit", defaultValue = "20") int limit,
                            @RequestParam(name = "sortName", defaultValue = "id") String sortName,
                            @RequestParam(name = "sortOrder", defaultValue = "DESC") Sort.Direction sortOrder) {
        Map<String, Object> params = new HashMap<>();
        MapUtils.putNotNull(params, RoleDao.KEYWORD, keyword);
        MapUtils.putNotNull(params, RoleDao.APP_ID, appId);
        PageTable pageTable = roleService.searchScrollPage(params, new Pagination(offset, limit, new Sort(new Sort.Order(sortOrder, sortName))));
        return StatusDto.buildSuccess(pageTable);
    }

    /**
     * 新建或更新应用角色
     *
     * @param role 角色信息
     * @return 返回更新结果
     */
    @PostMapping
    @Logger(module = com.rocoinfo.config.log.Type.APP_ROLE)
    public StatusDto createOrUpdate(@RequestBody @Validated Role role) {
        roleService.createOrUpdate(role);
        return StatusDto.buildSuccess("操作成功");
    }

    /**
     * 查询应用角色详情
     *
     * @param id 角色id
     * @return 返回角色信息
     */
    @GetMapping("/{id}/get")
    @Logger(module = com.rocoinfo.config.log.Type.APP_ROLE)
    public StatusDto getById(@PathVariable(value = "id") Long id) {
        return StatusDto.buildSuccess(roleService.getById(id));
    }

    /**
     * 删除角色
     *
     * @param id 角色id
     * @return 返回删除结果
     */
    @DeleteMapping("/{id}/del")
    @Logger(module = com.rocoinfo.config.log.Type.APP_ROLE)
    public StatusDto delete(@PathVariable(value = "id") Long id) {
        roleService.logicDelById(id);
        return StatusDto.buildSuccess("删除成功！");
    }

    /**
     * 根据角色id和appId获取该角色所属应用的所有权限，将已具有的权限标记出来
     *
     * @param id    角色id
     * @param appId 应用id
     * @return 返回权限信息
     */
    @GetMapping("/{id}/permissions")
    @Logger(module = com.rocoinfo.config.log.Type.APP_ROLE)
    public StatusDto permissions(@PathVariable(value = "id") Long id, @RequestParam(value = "appId") Long appId) {
        return StatusDto.buildSuccess(roleService.findPermissions(id, appId));

    }

    /**
     * 设置指定角色的权限
     *
     * @param id            角色id
     * @param permissionIds 权限Ids
     * @return 返回执行结果
     */
    @PostMapping("/{id}/setPermission")
    @Logger(module = com.rocoinfo.config.log.Type.APP_ROLE)
    public StatusDto updatePermission(@PathVariable(value = "id") Long id, @RequestParam(value = "permissionIds[]") List<Long> permissionIds) {
        roleService.updatePermission(id, permissionIds);
        return StatusDto.buildSuccess("修改角色权限成功！");

    }

}
