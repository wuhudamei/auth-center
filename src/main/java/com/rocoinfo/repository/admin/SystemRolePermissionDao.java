package com.rocoinfo.repository.admin;

import com.rocoinfo.common.persistence.CrudDao;
import com.rocoinfo.entity.admin.SystemPermission;
import com.rocoinfo.entity.admin.SystemRolePermission;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * <dl>
 * <dd>Description: 角色权限管理Dao</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/3/8 20:24</dd>
 * <dd>@author：Kong</dd>
 * </dl>
 */
@Repository
public interface SystemRolePermissionDao extends CrudDao {

    /**
     * 插入角色 权限信息
     *
     * @param roleId
     * @param permissionId
     */
    void insert(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    /**
     * 根绝角色id删除角色权限关联信息
     *
     * @param roleId 角色id
     */
    void deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 查找角色绑定的权限及未绑定权限
     *
     * @param roleId 角色id
     * @return
     */
    List<SystemPermission> findRolePermission(@Param("roleId") Long roleId);

    /**
     * 批量插入角色权限关联信息
     *
     * @param rolePermissions 角色权限关联信息
     */
    void batchInsert(@Param("rolePermissions") List<SystemRolePermission> rolePermissions);
}
