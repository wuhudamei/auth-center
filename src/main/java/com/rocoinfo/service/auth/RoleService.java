package com.rocoinfo.service.auth;

import com.rocoinfo.common.service.CrudService;
import com.rocoinfo.entity.auth.Permission;
import com.rocoinfo.entity.auth.Role;
import com.rocoinfo.repository.auth.RoleDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <dl>
 * <dd>Description: 应用角色Service</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/5/31 下午7:22</dd>
 * <dd>@author：Kong</dd>
 * </dl>
 */
@Service
public class RoleService extends CrudService<RoleDao, Role> {

    @Autowired
    private RolePermissionService rolePermissionService;

    /**
     * 创建或更新应用角色信息
     *
     * @param role 角色信息
     */
    public void createOrUpdate(Role role) {
        if (role != null) {
            if (role.getId() != null) {
                this.entityDao.update(role);
            } else {
                role.setDeleted((byte) 0);
                super.insert(role);
            }
        }
    }

    /**
     * 逻辑删除
     *
     * @param id 角色id
     */
    public void logicDelById(Long id) {
        if (id != null) {
            Role role = new Role(id);
            role.setDeleted((byte) 1);
            super.update(role);
        }
    }

    /**
     * 通过角色id应用id获取该角色权限信息
     *
     * @param id    角色id
     * @param appId 应用id
     * @return 返回该角色的权限信息
     */
    public Map<String, List<Permission>> findPermissions(Long id, Long appId) {
        if (id != null || appId != null) {
            return rolePermissionService.findRolePermission(id, appId);
        }
        return Collections.emptyMap();
    }

    /**
     * 修改角色权限
     *
     * @param id            角色id
     * @param permissionIds 权限id
     */
    public void updatePermission(Long id, List<Long> permissionIds) {

        if (id != null && CollectionUtils.isNotEmpty(permissionIds)) {

            rolePermissionService.updatePermission(id, permissionIds);

        }
    }


    /**
     * 根据角色名称和应用id查询 角色
     * @param roleName
     * @param appId
     * @return
     */
    public Role getRoleByNameAndAppId(String roleName,Long appId){
        return entityDao.selectRoleByNameAndAppId(roleName, appId);
    }
}