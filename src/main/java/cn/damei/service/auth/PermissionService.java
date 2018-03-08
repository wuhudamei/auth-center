package cn.damei.service.auth;

import cn.damei.common.service.CrudService;
import cn.damei.entity.auth.Permission;
import cn.damei.dto.app.PermissionTreeDto;
import cn.damei.repository.auth.PermissionDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Service
public class PermissionService extends CrudService<PermissionDao, Permission> {


    public PermissionTreeDto createOrUpdate(Permission permission) {
        if (permission != null) {
            if (permission.getId() != null) {
                this.entityDao.update(permission);
            } else {
                this.entityDao.insert(permission);
            }
            return buildPermissionTree(permission,Boolean.FALSE);
        }
        return null;
    }


    public PermissionTreeDto findPermissionTree(Long appId) {
        if (appId != null) {
            List<Permission> permissions = this.entityDao.findByAppId(appId);
            if (CollectionUtils.isNotEmpty(permissions)) {
                Map<Long, List<Permission>> partPermissions = permissions.stream().collect(groupingBy(Permission::getPid));
                List<Permission> roots = partPermissions.get(0L);
                //保证根节点有且仅有一个
                if (CollectionUtils.isNotEmpty(roots) && roots.size() == 1) {
                    //将Permission对象构建成PermissionTree对象
                    PermissionTreeDto rootTree = buildPermissionTree(roots.get(0), Boolean.TRUE);
                    buildChildren(partPermissions, rootTree);
                    return rootTree;
                }

            }
        }
        return null;
    }


    private PermissionTreeDto buildPermissionTree(Permission permission, Boolean opened) {
        PermissionTreeDto permissionTreeDto = new PermissionTreeDto();
        if (permission != null) {
            permissionTreeDto.setId(permission.getId())
                    .setSort(permission.getSeq())
                    .setText(permission.getName())
                    .setOpened(opened);
        }
        return permissionTreeDto;
    }


    private void buildChildren(Map<Long, List<Permission>> partPermissions, PermissionTreeDto permissionTreeDto) {
        List<Permission> permissions = partPermissions.get(permissionTreeDto.getId());
        if (CollectionUtils.isNotEmpty(permissions)) {
            for(Permission permission: permissions){
                PermissionTreeDto subPermissionTreeDto = buildPermissionTree(permission,Boolean.FALSE);
                buildChildren(partPermissions,subPermissionTreeDto);
                permissionTreeDto.pushChildren(subPermissionTreeDto);
            }
        }
    }
}