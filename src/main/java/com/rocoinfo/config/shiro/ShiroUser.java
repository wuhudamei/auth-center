package com.rocoinfo.config.shiro;

import com.rocoinfo.entity.auth.App;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
 */
public class ShiroUser implements Serializable {

    private static final long serialVersionUID = -1473281454547002154L;
    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 姓名
     */
    private String name;

    /**
     * 角色信息
     */
    private List<String> roles;

    /**
     * 权限信息
     */
    private List<String> permissions;

    /**
     * 应用权限
     */
    private List<App> apps;

    /**
     * 员工工号
     */
    private String jobNum;

    /**
     * 员工 所在门店编码
     */
    private String storeCode;

    public ShiroUser(Long id) {
        this.id = id;
    }

    public ShiroUser(Long id, String username, String name, String jobNum, List<String> roles,
                     List<String> permissions, String storeCode) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.jobNum = jobNum;
        this.roles = roles;
        this.permissions = permissions;
        this.storeCode = storeCode;
    }

    /**
     * 判断对象是否具有角色
     *
     * @param role 指定角色
     * @return 具有返回 true 不具有返回 false
     */
    public boolean hasRole(String role) {
        if (StringUtils.isEmpty(role)) {
            return true;
        }
        if (CollectionUtils.isEmpty(roles)) {
            return false;
        }
        return roles.contains(role);
    }

    /**
     * 判断对象是否具有权限
     *
     * @param permission 指定权限
     * @return 具有返回 true 不具有返回 false
     */
    public boolean hasPermission(String permission) {
        if (StringUtils.isEmpty(permission)) {
            return true;
        }
        if (CollectionUtils.isEmpty(permissions)) {
            return false;
        }
        return roles.contains(permission);
    }

    /**
     * 本函数输出将作为默认的<shiro:principal/>输出.
     */
    @Override
    public String toString() {
        return username;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ShiroUser other = (ShiroUser) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public Long getId() {
        return id;
    }

    public ShiroUser setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public ShiroUser setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getName() {
        return name;
    }

    public ShiroUser setName(String name) {
        this.name = name;
        return this;
    }

    public List<String> getRoles() {
        return roles;
    }

    public ShiroUser setRoles(List<String> roles) {
        this.roles = roles;
        return this;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public ShiroUser setPermissions(List<String> permissions) {
        this.permissions = permissions;
        return this;
    }

    public List<App> getApps() {
        return apps;
    }

    public ShiroUser setApps(List<App> apps) {
        this.apps = apps;
        return this;
    }

    public String getJobNum() {
        return jobNum;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }
}