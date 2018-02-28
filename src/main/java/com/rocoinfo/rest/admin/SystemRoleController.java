package com.rocoinfo.rest.admin;

import com.rocoinfo.aop.logger.Logger;
import com.rocoinfo.common.BaseController;
import com.rocoinfo.config.log.Type;
import com.rocoinfo.dto.StatusDto;
import com.rocoinfo.dto.page.PageTable;
import com.rocoinfo.dto.page.Pagination;
import com.rocoinfo.entity.admin.SystemPermission;
import com.rocoinfo.entity.admin.SystemRole;
import com.rocoinfo.service.admin.SystemRoleService;
import com.rocoinfo.utils.MapUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * <dl>
 * <dd>Description: 管理员角色Controller</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/5/31 上午10:54</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
@RestController
@RequestMapping(value = "/api/system/role")
public class SystemRoleController extends BaseController {

    @Autowired
    private SystemRoleService roleService;

    /**
     * 角色列表
     *
     * @param keyword 查询关键字
     * @param offset  偏移量
     * @param limit   每页大小
     * @return
     */
    @GetMapping
    @Logger(module = Type.SYSTEM_ROLE)
    public Object search(@RequestParam(required = false) String keyword,
                         @RequestParam(defaultValue = "0") int offset,
                         @RequestParam(defaultValue = "20") int limit) {
        Map<String, Object> params = new HashMap<>();
        MapUtils.putNotNull(params, "keyword", keyword);
        PageTable<SystemRole> pageTable = this.roleService.searchScrollPage(params, new Pagination(offset, limit));
        return StatusDto.buildSuccess(pageTable);
    }

    /**
     * 添加或编辑角色信息
     *
     * @param role 角色信息
     * @return
     */
    @PostMapping
    @Logger(module = Type.SYSTEM_ROLE)
    public Object saveOrUpdate(@Validated SystemRole role) {
        return roleService.saveOrUpdate(role);
    }

    /**
     * 根据id查询角色信息
     *
     * @param id 角色id
     * @return
     */
    @GetMapping(value = "/{id}")
    @Logger(module = Type.SYSTEM_ROLE)
    public Object getById(@PathVariable Long id) {
        return StatusDto.buildSuccess(this.roleService.getById(id));
    }

    /**
     * 删除角色
     */
    @DeleteMapping(value = "/{id}")
    @Logger(module = Type.SYSTEM_ROLE)
    public Object delete(@PathVariable Long id) {
        return this.roleService.deleteRole(id);
    }

    /**
     * 查询角色绑定的权限及未绑定权限
     *
     * @param id 角色id
     * @return
     */
    @GetMapping(value = "/permission/{id}")
    @Logger(module = Type.SYSTEM_ROLE)
    public Object findRolePermission(@PathVariable("id") Long id) {
        Map<String, List<SystemPermission>> modulePermsMap = new LinkedHashMap<>();
        List<SystemPermission> allPermission = this.roleService.findRolePermission(id);
        for (SystemPermission perm : allPermission) {
            String module = perm.getModule();

            List<SystemPermission> permList = modulePermsMap.get(module);
            if (CollectionUtils.isEmpty(permList)) {
                permList = new ArrayList<>();
                modulePermsMap.put(module, permList);
            }
            permList.add(perm);
        }

        return StatusDto.buildSuccess(modulePermsMap);
    }

    /**
     * 设置角色权限
     *
     * @param id            主键
     * @param permissionIds 权限ids
     * @return
     */
    @PostMapping(value = "/permission/{id}")
    @Logger(module = Type.SYSTEM_ROLE)
    public Object setRolePermissions(@PathVariable Long id, @RequestParam("permissions[]") List<Long> permissionIds) {
        if (CollectionUtils.isEmpty(permissionIds))
            return StatusDto.buildFailure("角色为空！");
        return this.roleService.setRolePermissions(id, permissionIds);
    }
}
