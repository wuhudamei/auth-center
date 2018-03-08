package cn.damei.entity.auth;

import cn.damei.entity.IdEntity;
import cn.damei.enumeration.Status;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;


public class User extends IdEntity {

    private static final long serialVersionUID = 302824385270333140L;


    @NotEmpty(message = "用户名不能为空")
    @Length(max = 20, message = "用户名长度不能超过20")
    private String username;


    private String plainPassword;


    private String password;


    private String salt;


    @NotEmpty(message = "姓名不能为空")
    @Length(max = 20, message = "姓名长度不超过20")
    private String name;


    @NotEmpty(message = "手机号不能为空")
    @Length(max = 20, message = "手机号长度不超过20")
    private String mobile;


    @Length(max = 50, message = "邮箱长度不超过20")
    private String email;


    private Status status;


    private Boolean deleted;


    @Transient
    private List<App> apps;


    @Transient
    private List<Role> roles;


    @Transient
    private List<Long> appIds;


    private String jobNum;



    private String orgCode;


    private String depCode;


    private String storeCode;


    private String headimgurl;



    private Integer positionId;


    private Integer ifStoreFlag;


    public User() {
        super();
    }

    public User(Long id) {
        super();
        this.id = id;
    }


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