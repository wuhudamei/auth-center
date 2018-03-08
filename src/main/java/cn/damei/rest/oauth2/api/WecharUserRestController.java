package cn.damei.rest.oauth2.api;

import cn.damei.common.oauth2.BaseOAuthController;
import cn.damei.common.oauth2.OAuthError;
import cn.damei.common.oauth2.OAuthResponse;
import cn.damei.service.auth.UserWechatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;


@RestController
@RequestMapping(value = "/oauth/api/wechatuser")
public class WecharUserRestController extends BaseOAuthController {

    @Autowired
    private UserWechatService userWechatService;


    @PostMapping(value = "/findBindsByJobNums")
    public OAuthResponse findOpenIdsByJobNums(@RequestParam(required = false) String appid,
                                              @RequestParam(value = "access_token", required = false) String accessToken,
                                              @RequestParam(value = "job_nums", required = false) List<String> jobNums) {

        //校验accessToken的合法性
        if(!oAuthService.validateAppToken(accessToken,appid)){
            return OAuthResponse.buildError(OAuthError.INVALID_APP_ACCESS_TOKEN);
        }
        Map<String,Set<String>> wecharUsers = userWechatService.findOpenidsByJobNums(jobNums);

        return OAuthResponse.buildSuccess().put("userInfo",wecharUsers);
    }

}
