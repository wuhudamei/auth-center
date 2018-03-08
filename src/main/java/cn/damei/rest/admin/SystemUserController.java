package cn.damei.rest.admin;

import cn.damei.config.shiro.ShiroUser;
import cn.damei.dto.StatusDto;
import cn.damei.entity.admin.SystemUser;
import cn.damei.enumeration.Status;
import cn.damei.service.admin.SystemRoleService;
import cn.damei.service.admin.SystemUserAppService;
import cn.damei.utils.MapUtils;
import cn.damei.common.BaseController;
import cn.damei.dto.page.PageTable;
import cn.damei.dto.page.Pagination;
import cn.damei.entity.admin.SystemRole;
import cn.damei.service.admin.SystemUserService;
import cn.damei.utils.WebUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/system/user")
public class SystemUserController extends BaseController {

    @Autowired
    private SystemUserService userService;
    @Autowired
    private SystemRoleService roleService;
    @Autowired
    private SystemUserAppService userAppService;


    @GetMapping
    public Object search(@RequestParam(required = false) String keyword,
                         @RequestParam(required = false) Status status,
                         @RequestParam(defaultValue = "0") int offset,
                         @RequestParam(defaultValue = "20") int limit) {
        Map<String, Object> params = new HashMap<>();
        MapUtils.putNotNull(params, "keyword", keyword);
        MapUtils.putNotNull(params, "status", status);
        PageTable<SystemRole> pageTable = this.userService.searchScrollPage(params, new Pagination(offset, limit));
        return StatusDto.buildSuccess(pageTable);
    }


    @PostMapping
    public Object create(@Validated SystemUser user) {
        return this.userService.create(user);
    }


    @PutMapping
    public Object edit(@RequestBody @Validated SystemUser user) {
        return this.userService.edit(user);
    }


    @GetMapping("/{id}")
    public Object getById(@PathVariable Long id) {
        return StatusDto.buildSuccess(this.userService.getById(id));
    }


    @GetMapping("/{id}/password")
    public Object resetPwd(@PathVariable Long id) {
        return this.userService.resetPwd(id);
    }


    @GetMapping("/role/{id}")
    public Object getRolesById(@PathVariable Long id) {
        return StatusDto.buildSuccess(this.roleService.getRolesByUserId(id));
    }


    @PostMapping("/role/{id}")
    public Object setRolesById(@PathVariable Long id, @RequestParam("roles[]") List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return StatusDto.buildFailure("请至少选择一个角色");
        }
        return this.roleService.setRolesByUserId(id, roleIds);
    }


    @PostMapping("/status/{id}")
    public Object switchStatus(@PathVariable Long id, @RequestParam Status status) {
        SystemUser user = new SystemUser(id);
        user.setStatus(status);
        int i = this.userService.update(user);
        if (i > 0) {
            return StatusDto.buildSuccess();
        }
        return StatusDto.buildFailure();
    }


    @DeleteMapping("/{id}")
    public Object deleteById(@PathVariable Long id) {
        this.userService.deleteById(id);
        return StatusDto.buildSuccess();
    }


    @GetMapping("/app/{id}")
    public Object getAppsById(@PathVariable Long id) {
        return StatusDto.buildSuccess(this.userService.getAppsByUserId(id));
    }


    @PostMapping("/app/{id}")
    public Object setAppsById(@PathVariable Long id, @RequestParam(value = "apps[]", defaultValue = "") List<Long> appIds) {
        return this.userService.setAppsByUserId(id, appIds);
    }


    @GetMapping("/app")
    public Object getLoginUserApps() {
        return StatusDto.buildSuccess(this.userAppService.findAppsByUserId(WebUtils.getLoggedUserId()));
    }


    @PostMapping(value = "/password/modify")
    public Object updatePwd(@RequestParam("plainPwd") String oldPwd, @RequestParam("loginPwd") String newPwd, @RequestParam("confirmPwd") String confirmPwd) {
        if (StringUtils.isBlank(oldPwd)) {
            return StatusDto.buildFailure("原密码为空！");
        }
        if (StringUtils.isBlank(newPwd)) {
            return StatusDto.buildFailure("新密码为空！");
        }
        if (StringUtils.isBlank(confirmPwd)) {
            return StatusDto.buildFailure("确认密码为空！");
        }
        if (!newPwd.equals(confirmPwd)) {
            return StatusDto.buildFailure("两次密码输入不一致！");
        }
        ShiroUser user = WebUtils.getLoggedUser();
        return this.userService.modifyPwd(user.getId(), oldPwd, newPwd);
    }
}
