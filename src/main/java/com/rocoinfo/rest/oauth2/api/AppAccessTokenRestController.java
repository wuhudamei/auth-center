package com.rocoinfo.rest.oauth2.api;

import com.rocoinfo.common.oauth2.BaseOAuthController;
import com.rocoinfo.common.oauth2.OAuthError;
import com.rocoinfo.common.oauth2.OAuthResponse;
import com.rocoinfo.config.log.Type;
import com.rocoinfo.entity.auth.App;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <dl>
 * <dd>Description: 功能描述</dd>
 * <dd>Company: 大诚若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/22 10:53</dd>
 * <dd>@author：Kong</dd>
 * </dl>
 */
@RestController
@RequestMapping(value = "/oauth/appToken")
public class AppAccessTokenRestController extends BaseOAuthController {

    /**
     * 获取第三方应用级的AccessToken
     *
     * @param appid  应用id
     * @param secret 应用秘钥
     * @return 返回应用级的accessToken
     */
    @PostMapping
    @com.rocoinfo.aop.logger.Logger(module = Type.APP_TOKEN)
    public OAuthResponse grantAccessToken(@RequestParam(required = false) String appid,
                                          @RequestParam(required = false) String secret) {
        // 校验appid和secret
        App app = appService.getByAppidAndSecret(appid, secret);
        if (app == null || App.Status.LOCK.equals(app.getStatus())) {
            return OAuthResponse.buildError(OAuthError.INVALID_SECRET);
        }
        //获取accessToken
        String accessToken = oAuthService.grantAppToken(appid);

        return OAuthResponse.buildSuccess()
                .put("access_token", accessToken);
    }
}
