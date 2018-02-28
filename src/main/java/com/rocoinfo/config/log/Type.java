package com.rocoinfo.config.log;

/**
 * <dl>
 * <dd>Description: 日志记录模块名</dd>
 * <dd>Company: 大诚若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/30 10:48</dd>
 * <dd>@author：Kong</dd>
 * </dl>
 */
public class Type {

    /**
     * 默认模块
     */
    public static final String DEFAULT = "default";

    /**
     * 系统管理员模块
     */
    public static final String SYSTEM_USER = "SYSTEM_USER";

    /**
     * 系统角色模块
     */
    public static final String SYSTEM_ROLE = "SYSTEM_ROLE";

    /**
     * 应用模块
     */
    public static final String APP = "APP";

    /**
     * 应用用户模块
     */
    public static final String APP_USER = "APP_USER";

    /**
     * 应用权限模块
     */
    public static final String APP_PERMISSION = "APP_PERMISSION";

    /**
     * 应用角色模块
     */
    public static final String APP_ROLE = "APP_ROLE";

    /**
     * 用户模块
     */
    public static final String USER = "USER";

    /**
     * 用户登录
     */
    public static final String LOGIN = "LOGIN";

    /**
     * 用户登录
     */
    public static final String LOGOUT = "LOGOUT";

    /**
     * 用户获取Code
     */
    public static final String OAUTH_CODE = "OAUTH_CODE";

    /**
     * 用户获取AccessToken
     */
    public static final String OAUTH_TOEKN = "OAUTH_TOEKN";

    /**
     * 用户调用接口修改密码
     */
    public static final String OAUTH_USERNAME_PASSWORD = "OAUTH_USERNAME_PASSWORD";

    /**
     * 用户点击微信菜单登录
     */
    public static final String OAUTH_MENU = "OAUTH_MENU";

    /**
     * 用户点击微信菜单绑定
     */
    public static final String OAUTH_MENU_BIND = "OAUTH_MENU_BIND";

    /**
     * 用户扫码登录
     */
    public static final String OAUTH_QRCODE = "OAUTH_QRCODE";

    /**
     * 用户在第三方应用登出
     */
    public static final String OAUTH_LOGOUT = "OAUTH_LOGOUT";

    /**
     * oauth 第三方应用获取用户微信绑定接口
     */
    public static final String OAUTH_WECHATUSER = "OAUTH_WECHATUSER";

    /**
     * App 全局唯一的token
     */
    public static final String APP_TOKEN = "APP_TOKEN";
}
