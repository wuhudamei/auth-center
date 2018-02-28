package com.rocoinfo.common.oauth2;

/**
 * <dl>
 * <dd>Description: </dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/7 上午10:37</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
public class OAuthException extends Exception {

    public OAuthException() {
        super();
    }

    public OAuthException(String message) {
        super(message);
    }

    public OAuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public OAuthException(Throwable cause) {
        super(cause);
    }

    protected OAuthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
