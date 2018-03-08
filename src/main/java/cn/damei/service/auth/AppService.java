package cn.damei.service.auth;

import cn.damei.config.shiro.ShiroUser;
import cn.damei.common.service.CrudService;
import cn.damei.entity.admin.SystemUserApp;
import cn.damei.entity.auth.App;
import cn.damei.entity.auth.Permission;
import cn.damei.repository.auth.AppDao;
import cn.damei.service.admin.SystemUserAppService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Encodes;

import java.util.List;


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


    public void createOrUpdate(App app, ShiroUser logged) {
        if (app != null) {
            if (app.getId() != null) {
                this.entityDao.update(app);
            } else {
                this.insert(app, logged);
            }
        }
    }


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


    private Permission buildPermission(App app) {
        Permission permission = new Permission();
        permission.setAppId(app.getId());
        permission.setName("root");
        permission.setPermission("*");
        permission.setSeq(0);
        permission.setPid(0L);
        return permission;
    }


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


    public void logicDelById(Long id) {
        if (id != null) {
            App app = new App(id);
            app.setDeleted((byte) 1);
            super.update(app);
        }
    }


    public void changeStatus(Long id, App.Status status) {
        App app = new App(id);
        app.setStatus(status);
        this.entityDao.update(app);
    }


    public List<App> findAppWithRolesByUserIdAndAppIds(Long userId, List<Long> appIds) {
        if (userId != null && CollectionUtils.isNotEmpty(appIds)) {
            return this.entityDao.findAppWithRolesByUserIdAndAppIds(userId, appIds);
        }
        return null;
    }


    public App getByAppid(String appid) {
        if (StringUtils.isNotBlank(appid)) {
            return this.entityDao.getByAppid(appid);
        }
        return null;
    }


    public App getByAppidAndSecret(String appid, String secret) {
        if (StringUtils.isNoneBlank(appid, secret)) {
            return this.entityDao.getByAppidAndSecret(appid, secret);
        }
        return null;
    }
}