package cn.damei.rest.auth;

import cn.damei.dto.StatusDto;
import cn.damei.dto.page.Sort;
import cn.damei.entity.auth.Role;
import cn.damei.enumeration.Status;
import cn.damei.service.auth.UserService;
import cn.damei.utils.MapUtils;
import com.google.common.collect.Maps;
import cn.damei.common.BaseController;
import cn.damei.dto.page.PageTable;
import cn.damei.dto.page.Pagination;
import cn.damei.entity.auth.App;
import cn.damei.entity.auth.User;
import cn.damei.utils.WebUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/api/users")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;


    @GetMapping
    public Object search(@RequestParam(required = false) String keyword,
                         @RequestParam(required = false) Status status,
                         @RequestParam(defaultValue = "0") int offset,
                         @RequestParam(defaultValue = "20") int limit,
                         @RequestParam(defaultValue = "id") String sortName,
                         @RequestParam(defaultValue = "DESC") Sort.Direction sortOrder) {
        Map<String, Object> params = new HashMap<>();
        MapUtils.putNotNull(params, "keyword", keyword);
        MapUtils.putNotNull(params, "status", status);
        PageTable apps = userService.searchScrollPage(params, new Pagination(offset, limit, new Sort(new Sort.Order(sortOrder, sortName))));
        return StatusDto.buildSuccess(apps);
    }


    @PostMapping
    public Object create(@RequestBody @Validated User user) {
        return this.userService.create(user);
    }


    @PutMapping
    public Object edit(@RequestBody @Validated User user) {
        return this.userService.edit(user, WebUtils.getLoggedUserId());
    }


    @GetMapping("/{id}")
    public Object getById(@PathVariable Long id) {
        return StatusDto.buildSuccess(this.userService.getById(id));
    }


    @DeleteMapping("/{id}")
    public Object deleteById(@PathVariable Long id) {
        if (this.userService.deleteById(id) > 0) {
            return StatusDto.buildSuccess();
        }
        return StatusDto.buildFailure();
    }


    @GetMapping("/{id}/password")
    public Object resetPwd(@PathVariable Long id) {
        if (this.userService.resetPwd(id)) {
            return StatusDto.buildSuccess();
        }
        return StatusDto.buildFailure();
    }


    @PostMapping("/{id}/status")
    public Object switchStatus(@PathVariable Long id, @RequestParam Status status) {
        User user = new User(id);
        user.setStatus(status);
        if (this.userService.update(user) > 0) {
            return StatusDto.buildSuccess();
        }
        return StatusDto.buildFailure("操作失败");
    }


    @PostMapping("/{id}/app")
    public Object setUserApps(@PathVariable Long id, @RequestParam(value = "appIds[]", defaultValue = "") List<Long> appIds) {
        this.userService.editUserAppRelation(id, appIds, WebUtils.getLoggedUserId());
        return StatusDto.buildSuccess();
    }


    @GetMapping("/{id}/role")
    public Object getUserRoles(@PathVariable Long id) {
        // 查询包角色信息的应用信息
        List<App> apps = this.userService.getUserRoles(id, WebUtils.getLoggedUserId());
        // 处理数据
        if (CollectionUtils.isNotEmpty(apps)) {
            Map<String, List<Role>> roleMap = Maps.newHashMapWithExpectedSize(apps.size());
            for (App app : apps) {
                // 过滤掉垃圾数据
                List<Role> roles = app.getRoles()
                        .stream()
                        .filter((o) -> o.getId() != null && StringUtils.isNotBlank(o.getName()))
                        .map((o) -> o.setModule(app.getName()))
                        .collect(Collectors.toList());
                roleMap.put(app.getName(), roles);
            }
            return StatusDto.buildSuccess(roleMap);
        }
        return StatusDto.buildSuccess();
    }


    @PostMapping("/{id}/role")
    public Object setUserRoles(@PathVariable Long id, @RequestParam(value = "roleIds[]", defaultValue = "") List<Long> roleIds) {
        return this.userService.setUserRoles(id, roleIds, WebUtils.getLoggedUserId());
    }



    @GetMapping("/grpbyapprole")
    public Object statisticsUserByAppAndRole(){
        return StatusDto.buildSuccess(userService.groupUserByAppAndRole());
    }
}
