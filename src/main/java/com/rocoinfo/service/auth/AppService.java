package com.rocoinfo.service.auth;

import com.rocoinfo.common.service.CrudService;
import com.rocoinfo.config.shiro.ShiroUser;
import com.rocoinfo.entity.admin.SystemUserApp;
import com.rocoinfo.entity.auth.App;
import com.rocoinfo.entity.auth.Permission;
import com.rocoinfo.repository.auth.AppDao;
import com.rocoinfo.service.admin.SystemUserAppService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Encodes;

import java.util.List;

/**
 * <dl>
 * <dd>Description: 应用Service</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/5/31 上午10:50</dd>
 * <dd>@author：Kong</dd>
 * </dl>
 */
@Service
public class AppService extends CrudService<AppDao, App> {

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private SystemUserAppService userAppService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private UserRoleService userRoleService;

    /**
     * 创建或更新应用信息
     *
     * @param app    应用信息
     * @param logged 登录用户
     */
    public void createOrUpdate(App app, ShiroUser logged) {
        if (app != null) {
            if (app.getId() != null) {
                this.entityDao.update(app);
            } else {
                this.insert(app, logged);
            }
        }
    }

    /**
     * 新增应用、用户与用户关联管理、应用权限根节点
     *
     * @param app    应用信息
     * @param logged 登录用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(App app, ShiroUser logged) {

        app.setStatus(App.Status.OPENED);
        app.setDeleted((byte) 0);
        this.geneAppIdAndSecret(app);
        super.insert(app);

        Permission permission = buildPermission(app);
        permissionService.insert(permission);

//        //构建超级管理员角色，并与超级管理员绑定
//        Role role = new Role("embed", "超级管理员角色", app.getId(), (byte) 0, logged.getId(), new Date());
//        roleService.insert(role);
//        rolePermissionService.insert(new RolePermission(role.getId(), permission.getAppId()));
//        userRoleService.insert(new UserRole(1L, role.getId()));

        SystemUserApp userApp = new SystemUserApp(logged.getId(), app.getId());
        userAppService.insert(userApp);
        if (!Long.valueOf(1L).equals(logged.getId())) {
            userAppService.insert(new SystemUserApp(1L, app.getId()));
        }

    }

    /**
     * 构建当前应用的权限根节点
     *
     * @param app 应用
     * @return
     */
    private Permission buildPermission(App app) {
        Permission permission = new Permission();
        permission.setAppId(app.getId());
        permission.setName("root");
        permission.setPermission("*");
        permission.setSeq(0);
        permission.setPid(0L);
        return permission;
    }

    /**
     * 生成应用id及应用秘钥
     *
     * @param app 应用信息
     */
    private void geneAppIdAndSecret(App app) {
        if (app != null) {
            byte[] appids = Digests.generateSalt(9);
            String appid = Encodes.encodeHex(appids);
            byte[] appSecrets = Digests.generateSalt(16);
            String appSecret = Encodes.encodeHex(appSecrets);
            app.setAppid(appid);
            app.setSecret(appSecret);
        }
    }

    /**
     * 逻辑删除
     *
     * @param id 应用id
     */
    public void logicDelById(Long id) {
        if (id != null) {
            App app = new App(id);
            app.setDeleted((byte) 1);
            super.update(app);
        }
    }

    /**
     * 修改应用状态
     *
     * @param id     应用id
     * @param status 应用状态
     */
    public void changeStatus(Long id, App.Status status) {
        App app = new App(id);
        app.setStatus(status);
        this.entityDao.update(app);
    }

    /**
     * 根据userid和appids查询包含角色信息的apps
     *
     * @param userId 用户id
     * @param appIds 应用ids，用来确定应用的范围
     * @return
     */
    public List<App> findAppWithRolesByUserIdAndAppIds(Long userId, List<Long> appIds) {
        if (userId != null && CollectionUtils.isNotEmpty(appIds)) {
            return this.entityDao.findAppWithRolesByUserIdAndAppIds(userId, appIds);
        }
        return null;
    }

    /**
     * 根据appid查询
     *
     * @param appid appid
     * @return
     */
    public App getByAppid(String appid) {
        if (StringUtils.isNotBlank(appid)) {
            return this.entityDao.getByAppid(appid);
        }
        return null;
    }

    /**
     * 根据appid和secret查询
     *
     * @param appid  appid
     * @param secret 秘钥
     * @return
     */
    public App getByAppidAndSecret(String appid, String secret) {
        if (StringUtils.isNoneBlank(appid, secret)) {
            return this.entityDao.getByAppidAndSecret(appid, secret);
        }
        return null;
    }
}