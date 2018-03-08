package cn.damei.rest.oauth2.core;

import cn.damei.config.shiro.ShiroUser;
import cn.damei.dto.StatusDto;
import cn.damei.wx.WxHttpHandler;
import cn.damei.common.oauth2.BaseOAuthController;
import cn.damei.common.oauth2.OAuthError;
import cn.damei.common.oauth2.OAuthException;
import cn.damei.config.shiro.token.CustomUserNameToken.Type;
import cn.damei.entity.auth.App;
import cn.damei.entity.auth.User;
import cn.damei.entity.auth.UserWechat;
import cn.damei.service.auth.UserService;
import cn.damei.service.auth.UserWechatService;
import cn.damei.service.login.LoginService;
import cn.damei.utils.MapUtils;
import cn.damei.utils.WebUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/oauth/menu")
public class ClickWxMenuOAuthController extends BaseOAuthController {

    @Autowired
    private UserWechatService userWechatService;
    @Autowired
    private UserService userService;
    @Autowired
    private LoginService loginService;


    private static final String BIND_URL = "/oauth/bind";


    @GetMapping("/code")
    public ModelAndView grantCode(@RequestParam(required = false) String code,
                                  @RequestParam(required = false) String appid,
                                  @RequestParam(value = "redirect_url", required = false) String redirectUrl,
                                  @RequestParam(required = false) String state,
                                  Model model,
                                  HttpServletRequest request) {

        ModelAndView view = new ModelAndView(ERROR_PAGE);

        //通过appid获取对应的app信息
        if (StringUtils.isEmpty(code)) {
            model.addAttribute(ATTRIBUTE_ERROR_KEY, OAuthError.INVALID_WX_CODE);
            return view;
        }

        App app = appService.getByAppid(appid);
        if (app == null || App.Status.LOCK.equals(app.getStatus())) {
                model.addAttribute(ATTRIBUTE_ERROR_KEY, OAuthError.INVALID_APPID);
                return view;
            }

            // 获取用户的oepnid
            String openid = WxHttpHandler.getOpenId(app.getWxAppid(), app.getWxSecret(), code);
            if (StringUtils.isEmpty(openid)) {
                model.addAttribute(ATTRIBUTE_ERROR_KEY, OAuthError.GET_WX_OPENID_FAILED);
                return view;
            }

            //获取该微信绑定的账号
            UserWechat userWechat = userWechatService.findByOpenid(openid);
            if (userWechat == null || userWechat.getUserId() == null) {
                model.addAttribute("openid", openid);
                model.addAttribute("redirect", redirectUrl);
            model.addAttribute("state", state);
            //跳转到绑定页面
            return new ModelAndView(BIND_URL);
        }

        //查询用户信息，触发用户登录
        User user = userService.getById(userWechat.getUserId());
        if (user == null) {
            model.addAttribute(ATTRIBUTE_ERROR_KEY, OAuthError.NOT_FIND_USER);
            return view;
        }

        StatusDto res = loginService.login(user.getUsername(), null, Type.AUTH_USER, true);

        // 用户登录成功发放code并且重定向到对应的url
            if (res.isSuccess()) {
            try {
                String oauthCode = this.grantCode(user.getUsername());
                this.processCodeLog(code, request);
                return new ModelAndView(this.buildCodeRedirectUrl(true, redirectUrl, oauthCode, state));
            } catch (OAuthException e) {
                model.addAttribute(ATTRIBUTE_ERROR_KEY, OAuthError.GRANT_CODE_ERROR.getMessage());
                return view;
            }
        }

        WebUtils.getSession().setAttribute("appid", appid);
        model.addAttribute(ATTRIBUTE_ERROR_KEY, OAuthError.SYSTEM_ERROR);
        return view;
    }



    @RequestMapping("/bind")
    public Object bind(@RequestParam String username,
                       @RequestParam String password,
                       @RequestParam String openid,
                       @RequestParam String redirect,
                       @RequestParam String state,
                       HttpServletRequest request) {

        //校验该用户是否已经绑定，绑定过的用户不能再次绑定
        List<UserWechat> userWechats = userWechatService.findByOrgNum(username);
        if (CollectionUtils.isNotEmpty(userWechats)) {
            return StatusDto.buildFailure("该用户已经被绑定！");
        }

        //校验用户名密码
        StatusDto res = loginService.login(username, password, Type.AUTH_USER, false);

        if (res.isSuccess()) {
            // 获取session中的用户信息及跳转信息，并建立绑定关系
            ShiroUser loggedUser = WebUtils.getLoggedUser();
            UserWechat userWechat = userWechatService.findByOpenid(openid);
            if (userWechat == null) {
                userWechat = new UserWechat(openid, loggedUser.getId(), loggedUser.getJobNum());
                userWechatService.insert(userWechat);
            } else {
                userWechat.setJobNum(loggedUser.getJobNum());
                userWechat.setUserId(loggedUser.getId());
                userWechatService.update(userWechat);
            }

            //发放code，并建立code与用户的联系
            String oauthCode;
            try {
                oauthCode = this.grantCode(username);
            } catch (OAuthException e) {
                return StatusDto.buildFailure("发放授权码失败！");
            }
            this.processCodeLog(oauthCode, request);
            String url = this.buildCodeRedirectUrl(false, redirect, oauthCode, state);
            return StatusDto.buildSuccess(MapUtils.of("redirect", url));
        }
        return res;
    }
}
