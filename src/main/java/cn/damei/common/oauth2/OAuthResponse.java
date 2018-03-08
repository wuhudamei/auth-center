package cn.damei.common.oauth2;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;


public class OAuthResponse {


    private static final String SUCCESS_CODE = "1";
    private static final String SUCCESS_MESSAGE = "success!";


    private String code;


    private String message;


    private Map<String, Object> data;

    private OAuthResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }


    public static OAuthResponse buildError(OAuthError error) {
        return new OAuthResponse(error.getErrorCode(), error.getMessage());
    }


    public static OAuthResponse buildSuccess() {
        return new OAuthResponse(SUCCESS_CODE, SUCCESS_MESSAGE);
    }


    public OAuthResponse put(String key, Object value) {
        if (data == null) {
            data = Maps.newHashMap();
        }
        if (StringUtils.isNotBlank(key) && value != null) {
            data.put(key, value);
        }
        return this;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public boolean isSuccess() {
        if (SUCCESS_CODE.equals(code)) {
            return true;
        }
        return false;
    }
}
