package cn.damei.entity.admin;

import cn.damei.entity.IdEntity;
import cn.damei.enumeration.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;


public class SystemUser extends IdEntity {

    public SystemUser() {
    }

    public SystemUser(Long id) {
        this.id = id;
    }


    @NotEmpty(message = "用户名不能为空")
    @Length(max = 20, message = "用户名长度不能超过20")
    private String username;


    @JsonIgnore
    private String password;


    @Transient
    @JsonIgnore
    private String plainPassword;


    @JsonIgnore
    private String salt;


    @NotEmpty(message = "姓名不能为空")
    @Length(max = 20, message = "姓名长度不能超过20")
    private String name;


    @NotEmpty(message = "手机号不能为空")
    @Length(max = 20, message = "手机号长度不能超过20")
    private String mobile;


    @NotEmpty(message = "邮箱不能为空")
    @Length(max = 50, message = "邮箱长度不超过50")
    private String email;


    @NotEmpty(message = "部门不能为空")
    @Length(max = 20, message = "部门名称长度不能超过20")
    private String department;


    @NotEmpty(message = "公司不能为空")
    @Length(max = 20, message = "公司名称长度不能超过50")
    private String company;


    private Status status;


    private Boolean deleted;


    @Transient
    @JsonIgnore
    private List<SystemRole> roles;


    public List<String> getRoleNameList() {
        if (CollectionUtils.isEmpty(roles)) {
            return Collections.emptyList();
        }

        ArrayList<String> roleNames = Lists.newArrayListWithCapacity(roles.size());
        for (SystemRole role : roles) {
            roleNames.add(role.getName());
        }

        return roleNames;
    }


    public LinkedHashSet<String> getPermissions() {
        LinkedHashSet<String> permissions = new LinkedHashSet<>();
        if (CollectionUtils.isNotEmpty(roles)) {
            for (SystemRole role : roles) {
                List<SystemPermission> permission = role.getPermission();
                if (permission != null) {
                    for (SystemPermission perm : permission) {
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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

    public String getPlainPassword() {
        return plainPassword;
    }

    public void setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
    }

    public List<SystemRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SystemRole> roles) {
        this.roles = roles;
    }
}
