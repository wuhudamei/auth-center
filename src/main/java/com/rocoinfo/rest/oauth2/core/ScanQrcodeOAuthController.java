package com.rocoinfo.rest.oauth2.core;

import com.corundumstudio.socketio.SocketIOClient;
import com.rocoinfo.common.PropertyHolder;
import com.rocoinfo.common.oauth2.BaseOAuthController;
import com.rocoinfo.common.oauth2.OAuthError;
import com.rocoinfo.common.oauth2.OAuthException;
import com.rocoinfo.config.shiro.token.CustomUserNameToken;
import com.rocoinfo.dto.StatusDto;
import com.rocoinfo.entity.auth.App;
import com.rocoinfo.entity.auth.User;
import com.rocoinfo.entity.auth.UserWechat;
import com.rocoinfo.enumeration.Status;
import com.rocoinfo.service.auth.UserService;
import com.rocoinfo.service.auth.UserWechatService;
import com.rocoinfo.service.login.LoginService;
import com.rocoinfo.utils.JsonUtils;
import com.rocoinfo.utils.MapUtils;
import com.rocoinfo.utils.PasswordUtil;
import com.rocoinfo.utils.cache.SocketIOClientCache;
import com.rocoinfo.utils.cache.StringCache;
import com.rocoinfo.utils.des.DesUtil;
import com.rocoinfo.wx.WxHttpHandler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * <dl>
 * <dd>Description: 微信扫码登录回调相关处理流程</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/5 下午3:20</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
@RestController
@RequestMapping("/oauth/qrcode")
public class ScanQrcodeOAuthController extends BaseOAuthController {

    @Autowired
    private UserWechatService userWechatService;
    @Autowired
    private UserService userService;
    @Autowired
    private LoginService loginService;
    /**
     * 扫码绑定页面
     */
    private static final String BIND_URL = "/oauth/scan/bind";

    /**
     * 登录页面 socketIo 事件推送回调接口
     */
    private static final String SCAN_LOGIN_URL = "/oauth/qrcode/login";

    @RequestMapping
    @com.rocoinfo.aop.logger.Logger(module = com.rocoinfo.config.log.Type.OAUTH_QRCODE)
    public ModelAndView grantCode(@RequestParam(required = false) String code,
                                  @RequestParam(required = false) String state,
                                  @RequestParam(required = false) String appid,
                                  Model model,
                                  HttpServletRequest request) {

        ModelAndView view = new ModelAndView(ERROR_PAGE);

        // 校验微信传递的code
        if (StringUtils.isAnyBlank(code)) {
            model.addAttribute(ATTRIBUTE_ERROR_KEY, OAuthError.INVALID_WX_CODE);
            return view;
        }

        // 校验微信传递的state
        if (StringUtils.isAnyBlank(state)) {
            model.addAttribute(ATTRIBUTE_ERROR_KEY, OAuthError.INVALID_WX_STATE);
            return view;
        }

        String uuid = state;

        //通过appid获取对应的app信息
        App app = appService.getByAppid(appid);
        if (app == null || App.Status.LOCK.equals(app.getStatus())) {
            model.addAttribute(ATTRIBUTE_ERROR_KEY, OAuthError.INVALID_APPID);
            return view;
        }

        String openid = WxHttpHandler.getOpenId(app.getWxAppid(), app.getWxSecret(), code);

        //获取该微信绑定的账号
        UserWechat userWechat = userWechatService.findByOpenid(openid);
        if (userWechat == null || userWechat.getUserId() == null) {
            model.addAttribute("openid", openid);
            model.addAttribute("uuid", uuid);
            //跳转到绑定页面
            return new ModelAndView(BIND_URL);
        }

        User user = userService.getById(userWechat.getUserId());
        if (user == null || Status.LOCK.equals(user.getStatus())) {
            model.addAttribute(ATTRIBUTE_ERROR_KEY, OAuthError.NOT_FIND_USER);
            return view;
        }

        SocketIOClient client;
        client = SocketIOClientCache.get(uuid);
        if (client == null) {
            model.addAttribute(ATTRIBUTE_ERROR_KEY, OAuthError.SOCKET_ERROR);
            return view;
        }

        // 推消息
        this.sendEvent2Client(client, userWechat.getOpenid(), user.getUsername());

        //微信端直接跳转到成功页面
        view.setViewName(SUCCESS_PAGE);
        return view;
    }

    /**
     * 向客户端推送进行单点登录的消息，主要推送pc端用于单点登录的url
     *
     * @param client   SocketIOClient用来像客户端推送消息
     * @param openid   openid
     * @param username 用户名
     */
    private void sendEvent2Client(SocketIOClient client, String openid, String username) {
        // 为了保证登录的安全性，后台通过StringCache缓存一个openid和username的映射用来进行登录时的验证
        StringCache.put(openid, username);
        // 构建pc端用来单点登录的url
        String url = this.buildPcLoginUrl(PropertyHolder.getBaseurl(), openid, username);
        // 向客户端发送单点登录的消息
        client.sendEvent("req", JsonUtils.toJson(MapUtils.of("code", "1", "url", url)));
        client.disconnect();
    }

    /**
     * 构建pc端登录的url
     *
     * @param baseUrl  网站的基础url
     * @param openid
     * @param username
     * @return
     */
    private String buildPcLoginUrl(String baseUrl, String openid, String username) {
        String url = baseUrl + SCAN_LOGIN_URL;
        String params = DesUtil.encode(JsonUtils.toJson(MapUtils.of("openid", openid, "username", username)));
        try {
            // 需要进行urlencode，否则参数传递过程中+号会被转成空字符串导致DesUtil.decode(s)失败
            url += "?secret=" + URLEncoder.encode(params, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }


    /**
     * 扫码后pc端登录请求的接口
     *
     * @param secret 登录携带的秘钥
     * @return 验证成功重定向到，第三方应用提供的redirectUrl上
     */
    @RequestMapping("/login")
    @com.rocoinfo.aop.logger.Logger(module = com.rocoinfo.config.log.Type.OAUTH_QRCODE)
    public Object pcLogin(String secret,
                          String redirectUrl,
                          String state,
                          HttpServletRequest request) {

        if (StringUtils.isNotBlank(secret)) {
            // 将secret解析成map
            Map<String, String> params = JsonUtils.fromJsonAsMap(DesUtil.decode(secret), String.class, String.class);

            //获取openid及username
            String openid = params.get("openid");
            String username = params.get("username");
            // openid username不能为空，并且StringCache中必须有值
            if (StringUtils.isAnyBlank(openid, username, StringCache.get(openid))) {
                return StatusDto.buildFailure("秘钥无效，请返回重新扫码登录");
            }
            // 用户登录
            StatusDto res = loginService.login(username, null, CustomUserNameToken.Type.AUTH_USER, true);
            if (res.isSuccess()) {
                // 移除StringCache中的缓存,生成code，重定向到第三方应用提供的地址
                StringCache.remove(openid);
                String code;
                try {
                    code = this.grantCode(username);
                } catch (OAuthException e) {
                    return StatusDto.buildFailure("发放授权码失败");
                }

                this.processCodeLog(code, request);
                String url = this.buildCodeRedirectUrl(true, redirectUrl, code, state);

                return new ModelAndView(url);
            }
        }

        //重定向到错误页面
        return new ModelAndView("redirect:/auth/error");
    }


    @PostMapping("/bind")
    @com.rocoinfo.aop.logger.Logger(module = com.rocoinfo.config.log.Type.OAUTH_QRCODE)
    public Object bind(@RequestParam String username,
                       @RequestParam String password,
                       @RequestParam String openid,
                       @RequestParam String uuid) {

        //校验该用户是否已经绑定，绑定过的用户不能再次绑定
        List<UserWechat> userWechats = userWechatService.findByOrgNum(username);
        if (CollectionUtils.isNotEmpty(userWechats)) {
            return StatusDto.buildFailure("该用户已经被绑定！");
        }

        //获取session中的用户信息及跳转信息，并建立绑定关系
        User user = userService.getByUsername(username);

        if (user != null && user.getPassword().equals(PasswordUtil.entryptUserPassword(password, user.getSalt()))) {

            //建立账号与微信的绑定关系
            UserWechat userWechat = userWechatService.findByOpenid(openid);
            if (userWechat != null) {
                userWechat.setJobNum(user.getJobNum());
                userWechat.setUserId(user.getId());
                userWechatService.update(userWechat);
            } else {
                userWechat = new UserWechat(openid, user.getId(), user.getJobNum());
                userWechatService.insert(userWechat);
            }

            SocketIOClient client;
            client = SocketIOClientCache.get(uuid);
            if (client == null) {
                return StatusDto.buildFailure("信息超时，请刷新页面重新扫码登录");
            }
            // 推消息
            this.sendEvent2Client(client, userWechat.getOpenid(), user.getUsername());

            //微信端直接跳转到成功页面
            return StatusDto.buildSuccess(MapUtils.of("redirect", PropertyHolder.getBaseurl() + "/" + SUCCESS_PAGE));
        }
        return StatusDto.buildFailure("您的用户名或密码错误！");
    }
}
