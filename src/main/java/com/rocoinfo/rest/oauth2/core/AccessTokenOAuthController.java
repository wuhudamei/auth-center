package com.rocoinfo.rest.oauth2.core;

import com.google.common.collect.Maps;
import com.rocoinfo.aop.logger.utils.LoggerJsonUtils;
import com.rocoinfo.common.oauth2.BaseOAuthController;
import com.rocoinfo.common.oauth2.OAuthError;
import com.rocoinfo.common.oauth2.OAuthResponse;
import com.rocoinfo.config.log.Type;
import com.rocoinfo.entity.auth.App;
import com.rocoinfo.entity.auth.User;
import com.rocoinfo.log.OAuthLogger;
import com.rocoinfo.service.auth.UserService;
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

/**
 * <dl>
 * <dd>Description: 第三方应用使用code获取AccessToken接口</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/6 上午10:13</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
@RestController
@RequestMapping("/oauth/token")
public class AccessTokenOAuthController extends BaseOAuthController {

    private Logger slf4jLogger = LoggerFactory.getLogger("com.rocoinfo.oauth");

    @Autowired
    private UserService userService;

    /**
     * 根据code发放access_token
     *
     * @param appid  appid
     * @param secret 秘钥
     * @param code   code
     * @param scope  是否发放权限信息
     * @return
     */
    @PostMapping
    @com.rocoinfo.aop.logger.Logger(module = Type.OAUTH_TOEKN)
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
        /* 根据用户名和应用id查询用户的所有信息（包括权限、角色）;
         * sql中有逻辑,请注意:
         * 并查询oa_employee_org表,将对应的公司且是门店标记的组织机构编码返回 store_code
         *  如果其直属和兼职都是门店,那么就将其,号拼接起来;并且按照type排序
         *  ( DIRECTLY直属靠前,兼职PART_TIME靠后)*/
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
                slf4jLogger.info(LoggerJsonUtils.toJson(oAuthLogger));
            }
            response = this.buildSuccessTokenResponse(accessToken, user, scope);
        } else {
            response = OAuthResponse.buildError(OAuthError.GRANT_TOKEN_ERROR);
        }

        return response;
    }

    /**
     * 构建成功的发放AccessToken的响应
     *
     * @param accessToken AccessToken
     * @param user        用户信息
     * @param scope       是否携带权限
     * @return
     */
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
