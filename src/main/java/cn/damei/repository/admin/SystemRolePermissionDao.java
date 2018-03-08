package cn.damei.repository.admin;

import cn.damei.common.persistence.CrudDao;
import cn.damei.entity.admin.SystemRolePermission;
import cn.damei.entity.admin.SystemPermission;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;



@Repository
public interface SystemRolePermissionDao extends CrudDao {


    void insert(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);


    void deleteByRoleId(@Param("roleId") Long roleId);


    List<SystemPermission> findRolePermission(@Param("roleId") Long roleId);


    void batchInsert(@Param("rolePermissions") List<SystemRolePermission> rolePermissions);
}
