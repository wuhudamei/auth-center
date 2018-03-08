package cn.damei.repository.auth;

import cn.damei.common.persistence.CrudDao;
import cn.damei.entity.auth.Permission;
import cn.damei.entity.auth.RolePermission;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository(value = "AppRolePermissionDao")
public interface RolePermissionDao extends CrudDao<RolePermission> {


    int batchInsert(@Param(value = "roleId")Long roleId,@Param(value = "permissionIds") List<Long> permissionIds);


    List<Permission> findRolePermission(@Param(value = "roleId") Long roleId, @Param(value = "appId") Long appId);


    int clearByRoleId(@Param(value = "roleId") Long roleId);
}