package cn.damei.entity.admin;

import cn.damei.entity.IdEntity;


public class SystemUserApp extends IdEntity {


    private Long userId;


    private Long appId;

    public SystemUserApp(Long userId, Long appId) {
        this.userId = userId;
        this.appId = appId;
    }

    public Long getUserId() {
        return userId;
    }

    public SystemUserApp setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public Long getAppId() {
        return appId;
    }

    public SystemUserApp setAppId(Long appId) {
        this.appId = appId;
        return this;
    }
}
