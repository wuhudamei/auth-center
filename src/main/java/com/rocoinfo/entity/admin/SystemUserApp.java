package com.rocoinfo.entity.admin;

import com.rocoinfo.entity.IdEntity;

/**
 * <dl>
 * <dd>Description: 用户应用关联关系</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/5/31 下午7:35</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
public class SystemUserApp extends IdEntity {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 应用id
     */
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
