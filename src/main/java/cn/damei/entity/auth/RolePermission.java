package cn.damei.entity.auth;

import cn.damei.entity.IdEntity;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;


@Alias(value = "appRolePermission")
public class RolePermission extends IdEntity implements Serializable {

    private static final long serialVersionUID = 7197304970532902617L;


    private Long roleId;


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