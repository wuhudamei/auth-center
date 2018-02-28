package com.rocoinfo.entity.auth;

import com.rocoinfo.entity.IdEntity;

/**
 * <dl>
 * <dd>Description: 用户角色关联关系</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/2 上午10:33</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
public class UserRole extends IdEntity {

    public UserRole() {
    }

    public UserRole(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 角色id
     */
    private Long roleId;

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
