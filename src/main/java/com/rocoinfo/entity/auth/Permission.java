package com.rocoinfo.entity.auth;

import com.rocoinfo.entity.IdEntity;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

/**
 * <dl>
 * <dd>Description: 应用权限实体类</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/5/31 下午17:10</dd>
 * <dd>@author：Kong</dd>
 * </dl>
 */
@Alias(value = "appPermission")
public class Permission extends IdEntity implements Serializable {

    private static final long serialVersionUID = 6107942344749094011L;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限描述
     */
    private String description;

    /**
     * 权限的值
     */
    private String permission;

    /**
     * 权限排序值 按从大到小的倒叙排列
     */
    private Integer seq;

    /**
     * 父权限的id
     */
    private Long pid;

    /**
     * 应用的id
     */
    private Long appId;

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