package cn.damei.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import cn.damei.entity.admin.SystemUser;

import java.io.Serializable;
import java.util.Date;


public class IdEntity implements Serializable {

    private static final long serialVersionUID = -2716222356509348153L;


    protected Long id;


    protected SystemUser createUser;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    protected Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SystemUser getCreateUser() {
        return createUser;
    }

    public void setCreateUser(SystemUser createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
