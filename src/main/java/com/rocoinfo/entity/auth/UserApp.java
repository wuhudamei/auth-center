package com.rocoinfo.entity.auth;

import com.rocoinfo.entity.IdEntity;

/**
 * <dl>
 * <dd>Description: 用户应用关联关系表</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/1 下午1:52</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
public class UserApp extends IdEntity {

    public UserApp() {
    }

    public UserApp(Long userId, Long appId) {
        this.userId = userId;
        this.appId = appId;
    }

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 应用id
     */
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
