package com.rocoinfo.entity.auth;

import com.rocoinfo.entity.IdEntity;
import com.rocoinfo.enumeration.Status;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * <dl>
 * <dd>Description: 用户管理</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/1 上午10:32</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
public class User extends IdEntity {

    private static final long serialVersionUID = 302824385270333140L;

    /**
     * 用户名（对应oa中的org_code）
     */
    @NotEmpty(message = "用户名不能为空")
    @Length(max = 20, message = "用户名长度不能超过20")
    private String username;

    /**
     * 原始密码（未加密）
     */
    private String plainPassword;

    /**
     * 密码
     */
    private String password;

    /**
     * 盐值
     */
    private String salt;

    /**
     * 真实姓名
     */
    @NotEmpty(message = "姓名不能为空")
    @Length(max = 20, message = "姓名长度不超过20")
    private String name;

    /**
     * 手机号
     */
    @NotEmpty(message = "手机号不能为空")
    @Length(max = 20, message = "手机号长度不超过20")
    private String mobile;

    /**
     * 邮箱
     */
    @Length(max = 50, message = "邮箱长度不超过20")
    private String email;

    /**
     * 状态
     */
    private Status status;

    /**
     * 逻辑删除标志位
     */
    private Boolean deleted;

    /**
     * 用户拥有的app权限
     */
    @Transient
    private List<App> apps;

    /**
     * 用户角色
     */
    @Transient
    private List<Role> roles;

    /**
     * 页面接收用户传递的应用ids
     */
    @Transient
    private List<Long> appIds;

    /**
     * 工号
     */
    private String jobNum;


    /**
     * 5位集团码,以便以后扩展给予8位长度
     * 其实我也不明白是啥意思
     */
    private String orgCode;

    /**
     * 3位部门码,与集团码联合组成8位员工号码(频繁变动)
     * 其实我也不明白是啥意思
     */
    private String depCode;

    /**
     * 门店对应的编码,多个门店使用,号拼接在一起;(DIRECTLY直属靠前,兼职PART_TIME靠后)
     */
    private String storeCode;

    /**
     * 登录用户的 微信头像--来自wechat表
     */
    private String headimgurl;


    /**
     * 职位id
     */
    private Integer positionId;

    /**
     * 专职的机构是否是门店
     */
    private Integer ifStoreFlag;


    public User() {
        super();
    }

    public User(Long id) {
        super();
        this.id = id;
    }

    /**
     * 获取用户角色名称
     *
     * @return
     */
    public List<String> getRoleNameList() {
        if (roles == null || roles.size() == 0) {
            return Collections.emptyList();
        }

        ArrayList<String> roleNameList = new ArrayList<>();
        for (Role role : roles) {
            roleNameList.add(role.getName());
        }

        return roleNameList;
    }

    /**
     * 获取用户权限
     *
     * @return
     */
    public LinkedHashSet<String> getPermissions() {
        LinkedHashSet<String> permissions = new LinkedHashSet<>();
        if (roles != null) {
            for (Role role : roles) {
                List<Permission> permission = role.getPermissions();
                if (permission != null) {
                    for (Permission perm : permission) {
                        permissions.add(perm.getPermission());
                    }
                }
            }
        }
        return permissions;
    }

    public String getUsername() {
        return username;
    }

    public String getPlainPassword() {
        return plainPassword;
    }

    public User setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public List<App> getApps() {
        return apps;
    }

    public void setApps(List<App> apps) {
        this.apps = apps;
    }

    public List<Long> getAppIds() {
        return appIds;
    }

    public void setAppIds(List<Long> appIds) {
        this.appIds = appIds;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public User setRoles(List<Role> roles) {
        this.roles = roles;
        return this;
    }

    public String getJobNum() {
        return jobNum;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getDepCode() {
        return depCode;
    }

    public void setDepCode(String depCode) {
        this.depCode = depCode;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public Integer getIfStoreFlag() {
        return ifStoreFlag;
    }

    public void setIfStoreFlag(Integer ifStoreFlag) {
        this.ifStoreFlag = ifStoreFlag;
    }
}