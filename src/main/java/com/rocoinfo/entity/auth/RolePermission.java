package com.rocoinfo.entity.auth;

import com.rocoinfo.entity.IdEntity;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

/**
 * <dl>
 * <dd>Description: 应用角色权限实体类</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/5/31 下午17:12</dd>
 * <dd>@author：Kong</dd>
 * </dl>
 */
@Alias(value = "appRolePermission")
public class RolePermission extends IdEntity implements Serializable {

    private static final long serialVersionUID = 7197304970532902617L;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 权限id
     */
    private Long permissionId;

    public RolePermission() {
    }

    public RolePermission(Long roleId, Long permissionId) {
        this.roleId = roleId;
        this.permissionId = permissionId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }
}