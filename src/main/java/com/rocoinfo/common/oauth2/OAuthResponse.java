package com.rocoinfo.common.oauth2;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * <dl>
 * <dd>Description: </dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/5 下午2:52</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
public class OAuthResponse {

    /**
     * 成功状态码
     */
    private static final String SUCCESS_CODE = "1";
    private static final String SUCCESS_MESSAGE = "success!";

    /**
     * 状态码
     */
    private String code;

    /**
     * 提示消息
     */
    private String message;

    /**
     * 返回携带数据
     */
    private Map<String, Object> data;

    private OAuthResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 构建错误返回信息
     *
     * @param error 错误枚举类
     * @return
     */
    public static OAuthResponse buildError(OAuthError error) {
        return new OAuthResponse(error.getErrorCode(), error.getMessage());
    }

    /**
     * 构架你成功的返回信息
     *
     * @return
     */
    public static OAuthResponse buildSuccess() {
        return new OAuthResponse(SUCCESS_CODE, SUCCESS_MESSAGE);
    }

    /**
     * 存放数据 key value形式
     *
     * @param key   key
     * @param value value
     * @return
     */
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
