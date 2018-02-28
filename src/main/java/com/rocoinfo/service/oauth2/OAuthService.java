package com.rocoinfo.service.oauth2;

import com.rocoinfo.dto.StatusDto;
import com.rocoinfo.entity.auth.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <dl>
 * <dd>Description: </dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/5 下午1:21</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
@Service
public class OAuthService {

    @Autowired
    private AccessTokenManager accessTokenManager;

    @Autowired
    private AppAccessTokenManager appAccessTokenManager;

    @Autowired
    private CodeManager codeManager;

    /**
     * 发放code
     *
     * @param username 用户名
     * @return
     */
    public String grantCode(String username) {
        String code = this.codeManager.generate();
        codeManager.setToRedis(code);
        codeManager.buildCodeUsernameRelation(code, username);
        return code;
    }

    /**
     * 根据授权码查询对应的用户名
     *
     * @param code 授权码
     * @return
     */
    public String getUsernameByCode(String code) {
        return this.codeManager.getUsernameByCode(code);
    }

    /**
     * 校验code
     *
     * @param code 授权码
     * @return
     */
    public boolean validateCode(String code) {
        return this.codeManager.validate(code);
    }

    /**
     * 使授权码失效
     *
     * @param code 授权码
     */
    public void expireCode(String code) {
        this.codeManager.expireCode(code);
    }

    /**
     * 发放access_token
     *
     * @return
     */
    public String grantAccessToken(User user) {
        // 生成access_token
        String accessToken = this.accessTokenManager.generator();
        // 建立access_token和用户的关联关系
        this.accessTokenManager.buildTokenUserRelation(accessToken, user);
        // 建立用户和access_token的关联关系
        this.accessTokenManager.buildUserTokenRelation(user, accessToken);
        return accessToken;
    }

    /**
     * 删除用户的accessToken
     *
     * @param username 用户名
     * @return 删除结果
     */
    public boolean deleteAccessToken(String username) {

        if (StringUtils.isNotEmpty(username)) {
            String accessToken = this.accessTokenManager.getAccessTokenByUsername(username);
            if (StringUtils.isNotEmpty(accessToken)) {
                this.accessTokenManager.relieveTokenUserRelation(accessToken);
            }
            this.accessTokenManager.relieveUserTokenRelation(username);
        }

        return true;
    }

    /**
     * 校验access_token是否合法
     *
     * @param accessToekn access_token
     * @return
     */
    public boolean validateAccessToken(String accessToekn) {
        return false;
    }

    /**
     * 根据username获取access_token
     *
     * @param username 用户名
     * @return
     */
    public String getAccessTokenByUsername(String username) {
        return this.accessTokenManager.getAccessTokenByUsername(username);
    }

    /**
     * 发放第三方应用级的AccessToken
     *
     * @param appid 应用id
     * @return 返回accessToken
     */
    public String grantAppToken(String appid) {

        String accessToken = this.appAccessTokenManager.generator();
        appAccessTokenManager.buildTokenAppRelation(accessToken, appid);

        return accessToken;
    }

    /**
     * 验证指定第三方应用级的accessToken是否合法
     *
     * @param accessToken 第三方应用级的accessToken
     * @param appid       第三方应用的appid
     * @return 合法返回 true  否则返回 false
     */
    public boolean validateAppToken(String accessToken, String appid) {

        if (StringUtils.isNoneEmpty(accessToken, appid)) {
            String redisAppid = appAccessTokenManager.getAppidByAccessToken(accessToken);
            if (StringUtils.isNotEmpty(redisAppid)) {
                return redisAppid.equals(appid);
            }
        }

        return false;
    }
}
