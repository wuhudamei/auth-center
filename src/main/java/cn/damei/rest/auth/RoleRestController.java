package cn.damei.rest.auth;

import cn.damei.dto.StatusDto;
import cn.damei.dto.page.Sort;
import cn.damei.entity.auth.Role;
import cn.damei.service.auth.RoleService;
import cn.damei.utils.MapUtils;
import cn.damei.common.BaseController;
import cn.damei.dto.page.PageTable;
import cn.damei.dto.page.Pagination;
import cn.damei.repository.auth.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController(value = "appRoleRestController")
@RequestMapping("/api/app/roles")
public class RoleRestController extends BaseController {

    @Autowired
    private RoleService roleService;


    @GetMapping
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


    @PostMapping
    public StatusDto createOrUpdate(@RequestBody @Validated Role role) {
        roleService.createOrUpdate(role);
        return StatusDto.buildSuccess("操作成功");
    }


    @GetMapping("/{id}/get")
    public StatusDto getById(@PathVariable(value = "id") Long id) {
        return StatusDto.buildSuccess(roleService.getById(id));
    }


    @DeleteMapping("/{id}/del")
    public StatusDto delete(@PathVariable(value = "id") Long id) {
        roleService.logicDelById(id);
        return StatusDto.buildSuccess("删除成功！");
    }


    @GetMapping("/{id}/permissions")
    public StatusDto permissions(@PathVariable(value = "id") Long id, @RequestParam(value = "appId") Long appId) {
        return StatusDto.buildSuccess(roleService.findPermissions(id, appId));

    }


    @PostMapping("/{id}/setPermission")
    public StatusDto updatePermission(@PathVariable(value = "id") Long id, @RequestParam(value = "permissionIds[]") List<Long> permissionIds) {
        roleService.updatePermission(id, permissionIds);
        return StatusDto.buildSuccess("修改角色权限成功！");

    }

}
