package cn.damei.rest.oauth2.core;

import cn.damei.common.oauth2.BaseOAuthController;
import cn.damei.common.oauth2.OAuthError;
import cn.damei.entity.auth.User;
import cn.damei.log.OAuthLogger;
import cn.damei.service.auth.UserService;
import com.google.common.collect.Maps;
import cn.damei.common.oauth2.OAuthResponse;
import cn.damei.entity.auth.App;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@RestController
@RequestMapping("/oauth/token")
public class AccessTokenOAuthController extends BaseOAuthController {

    private Logger slf4jLogger = LoggerFactory.getLogger("cn.damei.oauth");

    @Autowired
    private UserService userService;


    @PostMapping
    public OAuthResponse grantAccessToken(@RequestParam(required = false) String appid,
                                          @RequestParam(required = false) String secret,
                                          @RequestParam(required = false) String code,
                                          @RequestParam(required = false, defaultValue = "false") boolean scope,
                                          HttpServletRequest request) {

        OAuthLogger oAuthLogger = this.buildBaseOAuthLogger(OAUTH_LOGIN_MODULE, request);

        // 校验appid和secret
        App app = this.getAppByAppidAndSecret(appid, secret);
        if (app == null || App.Status.LOCK.equals(app.getStatus())) {
            return OAuthResponse.buildError(OAuthError.INVALID_SECRET);
        }

        // 记录应用信息
        oAuthLogger.setAppid(app.getId());
        oAuthLogger.setAppname(app.getName());

        // 校验code
        String username;
        try {
            if (!this.oAuthService.validateCode(code)) {
                return OAuthResponse.buildError(OAuthError.INVALID_CODE);
            }

            // 根据code查询对应的username
            username = this.oAuthService.getUsernameByCode(code);
            if (StringUtils.isBlank(username)) {
                return OAuthResponse.buildError(OAuthError.EXPIRE_CODE);
            }
        } finally {
            //code只能使用一次
            this.oAuthService.expireCode(code);
        }

        // 查询用户信息

        User user = this.userService.getAllInfoByUsernameAndAppId(username, app.getId());
        if (user == null) {
            return OAuthResponse.buildError(OAuthError.NOT_FIND_USER);
        }

        oAuthLogger.setUsername(user.getUsername());

        // 判断用户是否有改应用的权限
        if (this.hasAppPermission(user.getApps(), app)) {
            return OAuthResponse.buildError(OAuthError.INVALID_PERMISSION);
        }

        // 上述校验成功 则生成accesstoken ，并返回用户信息
        String accessToken = this.oAuthService.grantAccessToken(user);
        OAuthResponse response;
        if (StringUtils.isNotBlank(accessToken)) {
            // 只有分配AccessToken成功的日志才会发送
            oAuthLogger.setResult(accessToken);
            Map<String, String> codeLogMap = this.getCodeLog(code);
            if (MapUtils.isNotEmpty(codeLogMap)) {
                oAuthLogger.setBrowser(codeLogMap.get("browser"));
                oAuthLogger.setIp(codeLogMap.get("ip"));
                oAuthLogger.setServer(codeLogMap.get("server"));
            }
            if (slf4jLogger.isInfoEnabled()) {
            }
            response = this.buildSuccessTokenResponse(accessToken, user, scope);
        } else {
            response = OAuthResponse.buildError(OAuthError.GRANT_TOKEN_ERROR);
        }

        return response;
    }


    private OAuthResponse buildSuccessTokenResponse(String accessToken, User user, boolean scope) {
        OAuthResponse response = OAuthResponse.buildSuccess()
                .put("access_token", accessToken)
                .put("userinfo", this.buildUserAsMap(user));

        // 判断是否需要返回权限
        if (scope) {
            response.put("roles", user.getRoleNameList());
            response.put("scope", user.getPermissions());
        }
        return response;
    }

    private Map<String, Object> buildUserAsMap(User user) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("id", user.getId());
        map.put("username", user.getJobNum());
        map.put("name", user.getName());
        map.put("mobile", user.getMobile());
        map.put("email", user.getEmail());
        map.put("orgCode", user.getOrgCode());
        map.put("jobNum", user.getJobNum());
        map.put("depCode", user.getDepCode());
        map.put("storeCode", user.getStoreCode());
        map.put("headimgurl", user.getHeadimgurl());
        map.put("storeFlag",user.getIfStoreFlag());
        map.put("positionId",user.getPositionId());
        return map;
    }
}
