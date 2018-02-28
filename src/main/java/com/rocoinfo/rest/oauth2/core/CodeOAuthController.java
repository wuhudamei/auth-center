package com.rocoinfo.rest.oauth2.core;

import com.rocoinfo.common.oauth2.BaseOAuthController;
import com.rocoinfo.common.oauth2.OAuthError;
import com.rocoinfo.common.oauth2.OAuthException;
import com.rocoinfo.config.log.Type;
import com.rocoinfo.config.shiro.ShiroUser;
import com.rocoinfo.entity.auth.App;
import com.rocoinfo.utils.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * <dl>
 * <dd>Description: 用户请求授权码的入口</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/7 上午10:23</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
@RestController
@RequestMapping("/oauth/code")
public class CodeOAuthController extends BaseOAuthController {

    /**
     * 登录页面
     */
    private static final String LOGIN_PAGE = "oauth/login";

    private static final String ATTRIBUTE_APPID_KEY = "appid";
    private static final String ATTRIBUTE_REDIRECT_URL_KEY = "redirect_url";
    private static final String ATTRIBUTE_STATE_KEY = "state";

    @GetMapping
    @com.rocoinfo.aop.logger.Logger(module = Type.OAUTH_CODE)
    public ModelAndView getCode(@RequestParam(required = false) String appid,
                                @RequestParam(value = "redirect_url", required = false) String redirectUrl,
                                @RequestParam(value = "state", required = false) String state,
                                Model model,
                                HttpServletRequest request) {

        ModelAndView view = new ModelAndView(ERROR_PAGE);

        // 获取appid并校验
        App app = this.getAppByAppid(appid);
        if (app == null || App.Status.LOCK.equals(app.getStatus())) {
            // 设置错误的日志信息
            model.addAttribute(ATTRIBUTE_ERROR_KEY, OAuthError.INVALID_APPID);
            return view;
        }


        // 验证redirect_url
        if (StringUtils.isBlank(redirectUrl)) {
            // 设置错误的日志信息
            model.addAttribute(ATTRIBUTE_ERROR_KEY, OAuthError.INVALID_REDIRECT_URL);
            return view;
        }

        // 如果用户已经登录,并且Token不为空 直接发放授权码，并跳转到回调的url
        ShiroUser loggedUser = WebUtils.getLoggedUser();
        if (loggedUser != null && oAuthService.getAccessTokenByUsername(loggedUser.getUsername()) != null) {
            try {
                String code = this.grantCode(loggedUser.getUsername());
                this.processCodeLog(code, request);
                return new ModelAndView(this.buildCodeRedirectUrl(true, redirectUrl, code, state));
            } catch (OAuthException e) {
                model.addAttribute(ATTRIBUTE_ERROR_KEY, OAuthError.GRANT_CODE_ERROR);
                return view;
            }
        }

        WebUtils.getSession().setAttribute(ATTRIBUTE_APPID_KEY, appid);
        // 如果没有登录
        model.addAttribute(ATTRIBUTE_APPID_KEY, appid);
        model.addAttribute(ATTRIBUTE_REDIRECT_URL_KEY, redirectUrl);
        model.addAttribute(ATTRIBUTE_STATE_KEY, state);
        return new ModelAndView(LOGIN_PAGE);

    }
}
