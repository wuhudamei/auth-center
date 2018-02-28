package com.rocoinfo.entity.auth;

import com.rocoinfo.entity.IdEntity;

/**
 * <dl>
 * <dd>Description: 用户微信绑定关系实体类</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/5 17:21</dd>
 * <dd>@author：Kong</dd>
 * </dl>
 */
public class UserWechat extends IdEntity {

    private static final long serialVersionUID = -6826934088623360654L;

    private Long id;

    /**
     * 微信openid
     */
    private String openid;

    /**
     * 工号
     */
    private String jobNum;

    /**
     * 用户id
     */
    private Long userId;

    public UserWechat() {
    }

    public UserWechat(String openid, Long userId,String jobNum) {
        this.openid = openid;
        this.userId = userId;
        this.jobNum = jobNum;
    }

    public String getJobNum() {
        return jobNum;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
