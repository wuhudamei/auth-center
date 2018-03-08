package cn.damei.rest.admin;

import cn.damei.dto.StatusDto;
import cn.damei.service.admin.SystemRoleService;
import cn.damei.common.BaseController;
import cn.damei.dto.page.PageTable;
import cn.damei.dto.page.Pagination;
import cn.damei.entity.admin.SystemPermission;
import cn.damei.entity.admin.SystemRole;
import cn.damei.utils.MapUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping(value = "/api/system/role")
public class SystemRoleController extends BaseController {

    @Autowired
    private SystemRoleService roleService;


    @GetMapping
    public Object search(@RequestParam(required = false) String keyword,
                         @RequestParam(defaultValue = "0") int offset,
                         @RequestParam(defaultValue = "20") int limit) {
        Map<String, Object> params = new HashMap<>();
        MapUtils.putNotNull(params, "keyword", keyword);
        PageTable<SystemRole> pageTable = this.roleService.searchScrollPage(params, new Pagination(offset, limit));
        return StatusDto.buildSuccess(pageTable);
    }


    @PostMapping
    public Object saveOrUpdate(@Validated SystemRole role) {
        return roleService.saveOrUpdate(role);
    }


    @GetMapping(value = "/{id}")
    public Object getById(@PathVariable Long id) {
        return StatusDto.buildSuccess(this.roleService.getById(id));
    }


    @DeleteMapping(value = "/{id}")
    public Object delete(@PathVariable Long id) {
        return this.roleService.deleteRole(id);
    }


    @GetMapping(value = "/permission/{id}")
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


    @PostMapping(value = "/permission/{id}")
    public Object setRolePermissions(@PathVariable Long id, @RequestParam("permissions[]") List<Long> permissionIds) {
        if (CollectionUtils.isEmpty(permissionIds)) {
            return StatusDto.buildFailure("角色为空！");
        }
        return this.roleService.setRolePermissions(id, permissionIds);
    }
}
