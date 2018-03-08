package cn.damei.entity.auth;

import cn.damei.entity.IdEntity;


public class UserWechat extends IdEntity {

    private static final long serialVersionUID = -6826934088623360654L;

    private Long id;


    private String openid;


    private String jobNum;


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
