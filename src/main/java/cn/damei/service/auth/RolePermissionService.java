package cn.damei.service.auth;

import cn.damei.common.service.CrudService;
import cn.damei.entity.auth.Permission;
import cn.damei.entity.auth.RolePermission;
import cn.damei.repository.auth.RolePermissionDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RolePermissionService extends CrudService<RolePermissionDao, RolePermission> {


    Map<String, List<Permission>> findRolePermission(Long id, Long appId) {
        if (id != null && appId != null) {

            List<Permission> permissions = this.entityDao.findRolePermission(id, appId);
            if (CollectionUtils.isNotEmpty(permissions)) {
                Map<Long, List<Permission>> permissionParts = buildPermissionParts(permissions);
                List<Permission> roots = permissionParts.get(0L);
                if (CollectionUtils.isNotEmpty(roots) && roots.size() == 1) {
                    Permission root = roots.get(0);
                    //获取根节点的子节点（即模块节点）
                    List<Permission> modules = permissionParts.get(root.getId());
                    if (CollectionUtils.isNotEmpty(modules)) {
                        Map<String, List<Permission>> reallyPermissions = new LinkedHashMap<>();
                        for (Permission module : modules) {
                            //获取模块节点的子节点（即：叶子节点）
                            List<Permission> leafs = permissionParts.computeIfAbsent(module.getId(),(key)-> new ArrayList<>());
                            leafs.add(0, module);
                            reallyPermissions.put(module.getName(), leafs);
                        }
                        return reallyPermissions;
                    }
                }

            }

        }
        return Collections.emptyMap();
    }


    private Map<Long, List<Permission>> buildPermissionParts(List<Permission> permissions) {
        if (CollectionUtils.isNotEmpty(permissions)) {
            Map<Long, List<Permission>> allPermissions = new LinkedHashMap<>();
            for (Permission permission : permissions) {
                Long pId = permission.getPid();
                List<Permission> partPermissions = allPermissions.computeIfAbsent(pId, (key) -> new ArrayList<>());
                partPermissions.add(permission);
            }
            return allPermissions;
        }
        return Collections.emptyMap();
    }


    @Transactional(rollbackFor = Exception.class)
    void updatePermission(Long roleId, List<Long> permissionIds) {
        if (roleId != null && CollectionUtils.isNotEmpty(permissionIds)) {
            this.entityDao.clearByRoleId(roleId);
            this.entityDao.batchInsert(roleId, permissionIds);
        }
    }
}