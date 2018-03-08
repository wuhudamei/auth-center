package cn.damei.service.auth;

import cn.damei.common.service.CrudService;
import cn.damei.entity.auth.Permission;
import cn.damei.entity.auth.Role;
import cn.damei.repository.auth.RoleDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;


@Service
public class RoleService extends CrudService<RoleDao, Role> {

    @Autowired
    private RolePermissionService rolePermissionService;


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


    public void logicDelById(Long id) {
        if (id != null) {
            Role role = new Role(id);
            role.setDeleted((byte) 1);
            super.update(role);
        }
    }


    public Map<String, List<Permission>> findPermissions(Long id, Long appId) {
        if (id != null || appId != null) {
            return rolePermissionService.findRolePermission(id, appId);
        }
        return Collections.emptyMap();
    }


    public void updatePermission(Long id, List<Long> permissionIds) {

        if (id != null && CollectionUtils.isNotEmpty(permissionIds)) {

            rolePermissionService.updatePermission(id, permissionIds);

        }
    }



    public Role getRoleByNameAndAppId(String roleName,Long appId){
        return entityDao.selectRoleByNameAndAppId(roleName, appId);
    }
}