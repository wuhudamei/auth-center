package com.rocoinfo.repository.auth;

import com.rocoinfo.common.persistence.CrudDao;
import com.rocoinfo.entity.auth.Permission;
import com.rocoinfo.entity.auth.RolePermission;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <dl>
 * <dd>Description: 应用权限Dao</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/5/31 下午17:12</dd>
 * <dd>@author：Kong</dd>
 * </dl>
 */
@Repository(value = "AppRolePermissionDao")
public interface RolePermissionDao extends CrudDao<RolePermission> {

    /**
     * 批量插入角色权限信息
     * @param roleId 角色id
     * @param permissionIds 权限ids
     * @return 返回执行条数
     */
    int batchInsert(@Param(value = "roleId")Long roleId,@Param(value = "permissionIds") List<Long> permissionIds);

    /**
     * 通过角色id应用id获取该角色权限信息
     *
     * @param roleId    角色id
     * @param appId 应用id
     * @return 返回该角色的权限信息
     */
    List<Permission> findRolePermission(@Param(value = "roleId") Long roleId, @Param(value = "appId") Long appId);

    /**
     * 清除角色权限信息
     * @param roleId 角色Id
     * @return 返回清除条数
     */
    int clearByRoleId(@Param(value = "roleId") Long roleId);
}