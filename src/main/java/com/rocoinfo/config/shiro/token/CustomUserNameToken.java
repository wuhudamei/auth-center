package com.rocoinfo.config.shiro.token;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * <dl>
 * <dd>Description: 为了区分系统用户还是外部用户以及是否是单点登录自定义的Token类</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/6 10:20</dd>
 * <dd>@author：Kong</dd>
 * </dl>
 */
public class CustomUserNameToken extends UsernamePasswordToken {

    private static final long serialVersionUID = 818288867621350715L;

    /**
     * 是否是单点登录
     */
    private boolean ssoLogin;

    /**
     * 用户类型，系统用户还是远程用户
     */
    private Type type;


    public CustomUserNameToken(String username, String password, Type type, boolean ssoLogin) {
        super(username, password);
        this.type = type;
        this.ssoLogin = ssoLogin;
    }

    public boolean isSsoLogin() {
        return ssoLogin;
    }

    public void setSsoLogin(boolean ssoLogin) {
        this.ssoLogin = ssoLogin;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {

        SYSTEM("系统用户"), AUTH_USER("外部用户");

        private String label;

        Type(String label) {
            this.label = label;
        }
    }
}
