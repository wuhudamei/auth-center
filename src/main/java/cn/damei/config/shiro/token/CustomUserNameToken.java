package cn.damei.config.shiro.token;

import org.apache.shiro.authc.UsernamePasswordToken;


public class CustomUserNameToken extends UsernamePasswordToken {

    private static final long serialVersionUID = 818288867621350715L;


    private boolean ssoLogin;


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
