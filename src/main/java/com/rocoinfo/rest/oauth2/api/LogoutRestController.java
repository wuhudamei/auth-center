package com.rocoinfo.rest.oauth2.api;

import com.rocoinfo.aop.logger.utils.LoggerJsonUtils;
import com.rocoinfo.common.oauth2.BaseOAuthController;
import com.rocoinfo.common.oauth2.OAuthError;
import com.rocoinfo.common.oauth2.OAuthResponse;
import com.rocoinfo.config.log.Type;
import com.rocoinfo.entity.auth.App;
import com.rocoinfo.log.OAuthLogger;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <dl>
 * <dd>Description: 第三方应用请求登出的接口</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/9 15:47</dd>
 * <dd>@author：Kong</dd>
 * </dl>
 */
@RestController
@RequestMapping("/oauth/logout")
public class LogoutRestController extends BaseOAuthController {

    private Logger slf4jLogger = LoggerFactory.getLogger("com.rocoinfo.oauth");


    /**
     * @param appid    第三方应用appid
     * @param secret   第三方应用secret
     * @param username 用户名
     * @return 返回登出结果
     */
    @PostMapping
    @com.rocoinfo.aop.logger.Logger(module = Type.OAUTH_LOGOUT)
    public OAuthResponse logout(@RequestParam(required = false) String appid,
                                @RequestParam(required = false) String secret,
                                @RequestParam(required = false) String username,
                                HttpServletRequest request) {
        OAuthLogger oAuthLogger = this.buildBaseOAuthLogger(OAUTH_LOGOUT_MODULE, request);

        // 校验appid和secret
        App app = this.appService.getByAppidAndSecret(appid, secret);
        if (app == null || App.Status.LOCK.equals(app.getStatus())) {
            return OAuthResponse.buildError(OAuthError.INVALID_SECRET);
        }

        oAuthLogger.setUsername(username);
        // 记录应用信息
        oAuthLogger.setAppid(app.getId());
        oAuthLogger.setAppname(app.getName());

        //删除用户accessToken
        if (StringUtils.isNotEmpty(username)) {
            this.oAuthService.deleteAccessToken(username);
        }

        if (slf4jLogger.isInfoEnabled()) {
            slf4jLogger.info(LoggerJsonUtils.toJson(oAuthLogger));
        }
        return OAuthResponse.buildSuccess();
    }

}
