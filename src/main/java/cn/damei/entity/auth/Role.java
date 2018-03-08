package cn.damei.entity.auth;

import cn.damei.entity.IdEntity;
import cn.damei.entity.admin.SystemUser;
import org.apache.ibatis.type.Alias;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Alias(value = "appRole")
public class Role extends IdEntity implements Serializable {

    private static final long serialVersionUID = 3282076168953359800L;


    private String name;


    private String description;


    private Long appId;


    private Byte deleted;


    @Transient
    private Boolean checked;


    @Transient
    private String module;


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