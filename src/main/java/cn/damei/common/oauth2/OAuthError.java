package cn.damei.common.oauth2;


public enum OAuthError {

    INVALID_APPID("400001", "无效的appid"),
    INVALID_SECRET("400002", "无效的secret"),
    INVALID_CODE("400003", "无效的code"),
    EXPIRE_CODE("400004", "code已失效"),
    NOT_FIND_USER("400005", "系统查询不到此用户"),
    INVALID_PERMISSION("400006", "系统没有此应用的权限"),
    GRANT_TOKEN_ERROR("400007", "发放access token失败"),
    GRANT_CODE_ERROR("400008", "发放code失败"),
    INVALID_USERNAME_PWD("400009", "用户名密码错误"),
    INVALID_REDIRECT_URL("400010", "无效的redirect_url"),
    INVALID_APP_ACCESS_TOKEN("400011", "无效的access_token"),
    INVALID_WX_CODE("400012", "无效的微信code"),
    INVALID_WX_STATE("400013", "无效的微信state"),
    GET_WX_OPENID_FAILED("400014", "从微信端获取用户openid失败"),
    SOCKET_ERROR("400015", "socket连接错误"),
    INVALID_ROLE_INAPP("400016", "该应用中没有此角色"),
    SYSTEM_ERROR("999999", "系统错误");

    private String errorCode;
    private String message;

    OAuthError(String errorCode, String message) {
        this.message = message;
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}
