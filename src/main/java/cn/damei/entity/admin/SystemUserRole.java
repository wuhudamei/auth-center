package cn.damei.entity.admin;

import cn.damei.entity.IdEntity;


public class SystemUserRole extends IdEntity {


    private Long userId;


    private Long roleId;

    public SystemUserRole() {
    }

    public SystemUserRole(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
