package cn.damei.rest.oauth2.core;

import cn.damei.common.oauth2.BaseOAuthController;
import cn.damei.config.shiro.token.CustomUserNameToken;
import cn.damei.dto.StatusDto;
import cn.damei.common.oauth2.OAuthError;
import cn.damei.common.oauth2.OAuthException;
import cn.damei.common.oauth2.OAuthResponse;
import cn.damei.entity.auth.App;
import cn.damei.service.login.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/oauth/username/password")
public class UsernamePasswordOAuthController extends BaseOAuthController {

    @Autowired
    private LoginService loginService;


    @PostMapping
    public Object grantCode(@RequestParam(required = false) String username,
                            @RequestParam(required = false) String password,
                            @RequestParam(required = false) String appid,
                            @RequestParam(value = "redirect_url", required = false) String redirectUrl,
                            @RequestParam(required = false) String state,
                            HttpServletRequest request) {


        // 校验用户名和密码
        if (StringUtils.isAnyBlank(username, password)) {
            return OAuthResponse.buildError(OAuthError.INVALID_USERNAME_PWD);
        }

        // 校验重定向的url
        if (StringUtils.isBlank(redirectUrl)) {
            return OAuthResponse.buildError(OAuthError.INVALID_REDIRECT_URL);
        }

        App app = this.getAppByAppid(appid);
        if (app == null || App.Status.LOCK.equals(app.getStatus())) {
            return OAuthResponse.buildError(OAuthError.INVALID_APPID);
        }

        // 用户登录
        StatusDto res = this.loginService.login(username, password, CustomUserNameToken.Type.AUTH_USER, false);
        if (!res.isSuccess()) {
            return res;
        }

        String code;
        try {
            code = this.grantCode(username);
            if (StringUtils.isBlank(code)) {
                return OAuthResponse.buildError(OAuthError.GRANT_CODE_ERROR);
            }
        } catch (OAuthException e) {
            return OAuthResponse.buildError(OAuthError.GRANT_CODE_ERROR);
        }

        this.processCodeLog(code, request);
        String url = this.buildCodeRedirectUrl(false, redirectUrl, code, state);
        return OAuthResponse.buildSuccess().put("redirect", url);
    }

}
