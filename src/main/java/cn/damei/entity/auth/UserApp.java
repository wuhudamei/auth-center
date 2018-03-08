package cn.damei.entity.auth;

import cn.damei.entity.IdEntity;


public class UserApp extends IdEntity {

    public UserApp() {
    }

    public UserApp(Long userId, Long appId) {
        this.userId = userId;
        this.appId = appId;
    }


    private Long userId;


    private Long appId;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }
}
