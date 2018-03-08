package cn.damei.entity.auth;

import cn.damei.entity.IdEntity;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;


@Alias(value = "appPermission")
public class Permission extends IdEntity implements Serializable {

    private static final long serialVersionUID = 6107942344749094011L;


    private String name;


    private String description;


    private String permission;


    private Integer seq;


    private Long pid;


    private Long appId;


    private Boolean checked;

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

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}