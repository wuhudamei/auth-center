package com.rocoinfo.entity.auth;

import com.rocoinfo.entity.IdEntity;
import com.rocoinfo.entity.admin.SystemUser;
import org.apache.ibatis.type.Alias;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <dl>
 * <dd>Description: 应用角色实体类</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/5/31 下午17:11</dd>
 * <dd>@author：Kong</dd>
 * </dl>
 */
@Alias(value = "appRole")
public class Role extends IdEntity implements Serializable {

    private static final long serialVersionUID = 3282076168953359800L;

    /**
     * 角色姓名
     */
    private String name;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 应用id
     */
    private Long appId;

    /**
     * 逻辑删除标志位
     */
    private Byte deleted;

    /**
     * 是否被选中
     */
    @Transient
    private Boolean checked;

    /**
     * 所属模块，设置用户角色时用
     */
    @Transient
    private String module;

    /**
     * 角色具有的权限
     */
    private List<Permission> permissions;


    public Role() {
    }

    public Role(String name, String description, Long appId, Byte deleted, Long createUserId, Date createTime) {
        this.name = name;
        this.description = description;
        this.appId = appId;
        this.deleted = deleted;
        this.createUser = new SystemUser(createUserId);
        this.createTime = createTime;

    }

    public Role(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Byte getDeleted() {
        return deleted;
    }

    public void setDeleted(Byte deleted) {
        this.deleted = deleted;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public String getModule() {
        return module;
    }

    public Role setModule(String module) {
        this.module = module;
        return this;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}