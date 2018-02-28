package com.rocoinfo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rocoinfo.entity.admin.SystemUser;

import java.io.Serializable;
import java.util.Date;

/**
 * <dl>
 * <dd>Description: </dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/3/8 下午5:10</dd>
 * <dd>@author：Aaron</dd>
 * </dl>git
 */
public class IdEntity implements Serializable {

    private static final long serialVersionUID = -2716222356509348153L;

    /**
     * 主键
     */
    protected Long id;

    /**
     * 创建人
     */
    protected SystemUser createUser;

    /**
     * 创建时间
     */
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
