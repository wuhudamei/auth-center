package com.rocoinfo.rest.oauth2.api;

import com.rocoinfo.common.oauth2.BaseOAuthController;
import com.rocoinfo.common.oauth2.OAuthError;
import com.rocoinfo.common.oauth2.OAuthResponse;
import com.rocoinfo.config.log.Type;
import com.rocoinfo.service.auth.UserWechatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <dl>
 * <dd>Description: 微信用户绑定的相关接口</dd>
 * <dd>Company: 大诚若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/22 11:02</dd>
 * <dd>@author：Kong</dd>
 * </dl>
 */
@RestController
@RequestMapping(value = "/oauth/api/wechatuser")
public class WecharUserRestController extends BaseOAuthController {

    @Autowired
    private UserWechatService userWechatService;

    /**
     * 通过jobNums获取用户绑定的微信openid
     *
     * @param appid       应用appid
     * @param accessToken 应用AccessToken
     * @param jobNums     用户jobNums
     * @return 返回用户jobNum与openIds的对应关系
     */
    @PostMapping(value = "/findBindsByJobNums")
    @com.rocoinfo.aop.logger.Logger(module = Type.OAUTH_WECHATUSER)
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
