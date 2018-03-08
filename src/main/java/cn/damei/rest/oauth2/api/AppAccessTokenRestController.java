package cn.damei.rest.oauth2.api;

import cn.damei.common.oauth2.BaseOAuthController;
import cn.damei.common.oauth2.OAuthError;
import cn.damei.common.oauth2.OAuthResponse;
import cn.damei.entity.auth.App;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/oauth/appToken")
public class AppAccessTokenRestController extends BaseOAuthController {


    @PostMapping
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
