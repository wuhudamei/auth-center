package cn.damei.rest.oauth2.api;

import cn.damei.common.oauth2.BaseOAuthController;
import cn.damei.common.oauth2.OAuthError;
import cn.damei.common.oauth2.OAuthResponse;
import cn.damei.entity.auth.App;
import cn.damei.log.OAuthLogger;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/oauth/logout")
public class LogoutRestController extends BaseOAuthController {

    private Logger slf4jLogger = LoggerFactory.getLogger("cn.damei.oauth");



    @PostMapping
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
        }
        return OAuthResponse.buildSuccess();
    }

}
