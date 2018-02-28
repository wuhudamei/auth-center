package com.rocoinfo.rest.admin;

import com.rocoinfo.aop.logger.Logger;
import com.rocoinfo.common.BaseController;
import com.rocoinfo.config.log.Type;
import com.rocoinfo.config.shiro.ShiroUser;
import com.rocoinfo.dto.StatusDto;
import com.rocoinfo.dto.page.PageTable;
import com.rocoinfo.dto.page.Pagination;
import com.rocoinfo.entity.admin.SystemRole;
import com.rocoinfo.entity.admin.SystemUser;
import com.rocoinfo.enumeration.Status;
import com.rocoinfo.service.admin.SystemRoleService;
import com.rocoinfo.service.admin.SystemUserAppService;
import com.rocoinfo.service.admin.SystemUserService;
import com.rocoinfo.utils.MapUtils;
import com.rocoinfo.utils.WebUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <dl>
 * <dd>Description: 系统设置 -> 管理员用户Controller</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/5/31 下午2:06</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
@RestController
@RequestMapping(value = "/api/system/user")
public class SystemUserController extends BaseController {

    @Autowired
    private SystemUserService userService;
    @Autowired
    private SystemRoleService roleService;
    @Autowired
    private SystemUserAppService userAppService;

    /**
     * 管理员用户列表
     *
     * @param keyword 查询关键字
     * @param offset  偏移量
     * @param limit   每页大小
     * @return
     */
    @GetMapping
    @Logger(module = Type.SYSTEM_USER)
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

    /**
     * 新建管理员用户
     *
     * @param user 管理员用户信息
     * @return
     */
    @PostMapping
    @Logger(module = Type.SYSTEM_USER)
    public Object create(@Validated SystemUser user) {
        return this.userService.create(user);
    }

    /**
     * 更新管理员用户
     *
     * @param user 管理员用户信息
     * @return
     */
    @PutMapping
    @Logger(module = Type.SYSTEM_USER)
    public Object edit(@RequestBody @Validated SystemUser user) {
        return this.userService.edit(user);
    }

    /**
     * 根据id查询用户信息
     *
     * @param id 用户id
     * @return
     */
    @GetMapping("/{id}")
    @Logger(module = Type.SYSTEM_USER)
    public Object getById(@PathVariable Long id) {
        return StatusDto.buildSuccess(this.userService.getById(id));
    }

    /**
     * 重置用户密码
     *
     * @param id 用户id
     * @return
     */
    @GetMapping("/{id}/password")
    @Logger(module = Type.SYSTEM_USER)
    public Object resetPwd(@PathVariable Long id) {
        return this.userService.resetPwd(id);
    }

    /**
     * 根据用户id查询用户角色信息
     *
     * @param id 用户id
     * @return
     */
    @GetMapping("/role/{id}")
    @Logger(module = Type.SYSTEM_USER)
    public Object getRolesById(@PathVariable Long id) {
        return StatusDto.buildSuccess(this.roleService.getRolesByUserId(id));
    }

    /**
     * 为用户设置角色信息
     *
     * @param id      用户id
     * @param roleIds 角色ids
     * @return
     */
    @PostMapping("/role/{id}")
    @Logger(module = Type.SYSTEM_USER)
    public Object setRolesById(@PathVariable Long id, @RequestParam("roles[]") List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return StatusDto.buildFailure("请至少选择一个角色");
        }
        return this.roleService.setRolesByUserId(id, roleIds);
    }

    /**
     * 用户状态改变
     *
     * @param id     用户id
     * @param status 改变后的状态
     * @return
     */
    @PostMapping("/status/{id}")
    @Logger(module = Type.SYSTEM_USER)
    public Object switchStatus(@PathVariable Long id, @RequestParam Status status) {
        SystemUser user = new SystemUser(id);
        user.setStatus(status);
        int i = this.userService.update(user);
        if (i > 0) {
            return StatusDto.buildSuccess();
        }
        return StatusDto.buildFailure();
    }

    /**
     * 根据id删除用户（逻辑删除）
     *
     * @param id 用户id
     * @return
     */
    @DeleteMapping("/{id}")
    @Logger(module = Type.SYSTEM_USER)
    public Object deleteById(@PathVariable Long id) {
        this.userService.deleteById(id);
        return StatusDto.buildSuccess();
    }

    /**
     * 根据用户id查询用户应用信息
     *
     * @param id 用户id
     * @return
     */
    @GetMapping("/app/{id}")
    @Logger(module = Type.SYSTEM_USER)
    public Object getAppsById(@PathVariable Long id) {
        return StatusDto.buildSuccess(this.userService.getAppsByUserId(id));
    }

    /**
     * 设置用户应用关联关系
     *
     * @param id     用户id
     * @param appIds 应用ids
     * @return
     */
    @PostMapping("/app/{id}")
    @Logger(module = Type.SYSTEM_USER)
    public Object setAppsById(@PathVariable Long id, @RequestParam(value = "apps[]", defaultValue = "") List<Long> appIds) {
        return this.userService.setAppsByUserId(id, appIds);
    }

    /**
     * 获取当前登录管理员用户所拥有的app权限
     *
     * @return
     */
    @GetMapping("/app")
    @Logger(module = Type.SYSTEM_USER)
    public Object getLoginUserApps() {
        return StatusDto.buildSuccess(this.userAppService.findAppsByUserId(WebUtils.getLoggedUserId()));
    }

    /**
     * 修改用户密码
     *
     * @param oldPwd     原密码
     * @param newPwd     新密码
     * @param confirmPwd 确认密码
     * @return
     */
    @PostMapping(value = "/password/modify")
    @Logger(module = Type.SYSTEM_USER)
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
