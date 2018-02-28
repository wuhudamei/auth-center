package com.rocoinfo.rest.auth;

import com.google.common.collect.Maps;
import com.rocoinfo.aop.logger.Logger;
import com.rocoinfo.common.BaseController;
import com.rocoinfo.config.log.Type;
import com.rocoinfo.dto.StatusDto;
import com.rocoinfo.dto.page.PageTable;
import com.rocoinfo.dto.page.Pagination;
import com.rocoinfo.dto.page.Sort;
import com.rocoinfo.entity.auth.App;
import com.rocoinfo.entity.auth.Role;
import com.rocoinfo.entity.auth.User;
import com.rocoinfo.enumeration.Status;
import com.rocoinfo.service.auth.UserService;
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
import java.util.stream.Collectors;

/**
 * <dl>
 * <dd>Description: </dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/1 上午10:44</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
@RestController
@RequestMapping(value = "/api/users")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 查询列表
     */
    @GetMapping
    @Logger(module = Type.USER)
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

    /**
     * 新增用户
     *
     * @param user 用户信息
     * @return
     */
    @PostMapping
    @Logger(module = Type.USER)
    public Object create(@RequestBody @Validated User user) {
        return this.userService.create(user);
    }

    /**
     * 编辑用户
     *
     * @param user
     * @return
     */
    @PutMapping
    @Logger(module = Type.USER)
    public Object edit(@RequestBody @Validated User user) {
        return this.userService.edit(user, WebUtils.getLoggedUserId());
    }

    /**
     * 根据id查询用户信息
     *
     * @param id 用户id
     * @return
     */
    @GetMapping("/{id}")
    @Logger(module = Type.USER)
    public Object getById(@PathVariable Long id) {
        return StatusDto.buildSuccess(this.userService.getById(id));
    }

    /**
     * 根据id删除用户
     *
     * @param id 用户id
     * @return
     */
    @DeleteMapping("/{id}")
    @Logger(module = Type.USER)
    public Object deleteById(@PathVariable Long id) {
        if (this.userService.deleteById(id) > 0) {
            return StatusDto.buildSuccess();
        }
        return StatusDto.buildFailure();
    }

    /**
     * 重置密码
     *
     * @param id 用户id
     * @return
     */
    @GetMapping("/{id}/password")
    @Logger(module = Type.USER)
    public Object resetPwd(@PathVariable Long id) {
        if (this.userService.resetPwd(id)) {
            return StatusDto.buildSuccess();
        }
        return StatusDto.buildFailure();
    }

    /**
     * 更改用户状态
     *
     * @param id     用户id
     * @param status 用户状态
     * @return
     */
    @PostMapping("/{id}/status")
    @Logger(module = Type.USER)
    public Object switchStatus(@PathVariable Long id, @RequestParam Status status) {
        User user = new User(id);
        user.setStatus(status);
        if (this.userService.update(user) > 0) {
            return StatusDto.buildSuccess();
        }
        return StatusDto.buildFailure("操作失败");
    }

    /**
     * 用户设置应用权限
     *
     * @param id     用户id
     * @param appIds 管理员选择的应用ids
     * @return
     */
    @PostMapping("/{id}/app")
    @Logger(module = Type.USER)
    public Object setUserApps(@PathVariable Long id, @RequestParam(value = "appIds[]", defaultValue = "") List<Long> appIds) {
        this.userService.editUserAppRelation(id, appIds, WebUtils.getLoggedUserId());
        return StatusDto.buildSuccess();
    }

    /**
     * 查询用户app权限的所有角色
     *
     * @param id 用户id
     * @return
     */
    @GetMapping("/{id}/role")
    @Logger(module = Type.USER)
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

    /**
     * 设置用户角色
     *
     * @param id      用户id
     * @param roleIds 角色ids
     * @return
     */
    @PostMapping("/{id}/role")
    @Logger(module = Type.USER)
    public Object setUserRoles(@PathVariable Long id, @RequestParam(value = "roleIds[]", defaultValue = "") List<Long> roleIds) {
        return this.userService.setUserRoles(id, roleIds, WebUtils.getLoggedUserId());
    }


    /**
     * 查询各个应用各个角色的员工
     * @return
     */
    @GetMapping("/grpbyapprole")
    public Object statisticsUserByAppAndRole(){
        return StatusDto.buildSuccess(userService.groupUserByAppAndRole());
    }
}
