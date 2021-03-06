package cn.damei.entity.admin;

import cn.damei.entity.IdEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;

import java.util.List;


public class SystemRole extends IdEntity {

    public SystemRole() {
    }

    public SystemRole(Long id) {
        this.id = id;
    }


    @NotEmpty(message = "名称不能为空")
    @Length(max = 20, message = "名称不能超过20个字符")
    private String name;


    @NotEmpty(message = "描述不能为空")
    @Length(max = 255, message = "描述信息不能超过255个字符")
    private String description;


    @Transient
    private Boolean checked;


    private List<SystemPermission> permission;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public List<SystemPermission> getPermission() {
        return permission;
    }

    public void setPermission(List<SystemPermission> permission) {
        this.permission = permission;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}