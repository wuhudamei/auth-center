package cn.damei.common.oauth2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;


public class OAuthRequest extends HttpServletRequestWrapper {

    private static final String PARAM_USERNAME = "username";
    private static final String PARAM_PASSWORD = "password";
    private static final String PARAM_APPID = "appid";
    private static final String PARAM_SECRET = "secret";
    private static final String PARAM_STATE = "state";
    private static final String PARAM_REDIRECT = "redirect_url";
    private static final String PARAM_CODE = "code";
    private static final String PARAM_SCOPE = "scope";


    public OAuthRequest(HttpServletRequest request) {
        super(request);
    }

    public String getUsername() {
        return this.getParameter(PARAM_USERNAME);
    }

    public String getPassword() {
        return this.getParameter(PARAM_PASSWORD);
    }

    public String getAppid() {
        return this.getParameter(PARAM_APPID);
    }

    public String getSecret() {
        return this.getParameter(PARAM_SECRET);
    }

    public String getState() {
        return this.getParameter(PARAM_STATE);
    }

    public String getRedirectUrl() {
        return this.getParameter(PARAM_REDIRECT);
    }

    public String getCode() {
        return this.getParameter(PARAM_CODE);
    }

    public boolean getScope() {
        return Boolean.valueOf(this.getParameter(PARAM_SCOPE));
    }
}
