package com.rocoinfo.entity.admin;

import com.rocoinfo.entity.IdEntity;

/**
 * <dl>
 * <dd>Description: 系统设置 -> 权限实体类</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/5/31 上午10:32</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
public class SystemPermission extends IdEntity {

    private static final long serialVersionUID = 7124970537818894923L;

    /**
     * 权限名
     */
    private String name;

    /**
     * 权限模块
     */
    private String module;

    /**
     * 权限值
     */
    private String permission;

    /**
     * 是否被选中
     */
    private Boolean checked;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}
