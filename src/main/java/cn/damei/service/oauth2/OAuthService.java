package cn.damei.service.oauth2;

import cn.damei.entity.auth.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OAuthService {

    @Autowired
    private AccessTokenManager accessTokenManager;

    @Autowired
    private AppAccessTokenManager appAccessTokenManager;

    @Autowired
    private CodeManager codeManager;


    public String grantCode(String username) {
        String code = this.codeManager.generate();
        codeManager.setToRedis(code);
        codeManager.buildCodeUsernameRelation(code, username);
        return code;
    }


    public String getUsernameByCode(String code) {
        return this.codeManager.getUsernameByCode(code);
    }


    public boolean validateCode(String code) {
        return this.codeManager.validate(code);
    }


    public void expireCode(String code) {
        this.codeManager.expireCode(code);
    }


    public String grantAccessToken(User user) {
        // 生成access_token
        String accessToken = this.accessTokenManager.generator();
        // 建立access_token和用户的关联关系
        this.accessTokenManager.buildTokenUserRelation(accessToken, user);
        // 建立用户和access_token的关联关系
        this.accessTokenManager.buildUserTokenRelation(user, accessToken);
        return accessToken;
    }


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


    public boolean validateAccessToken(String accessToekn) {
        return false;
    }


    public String getAccessTokenByUsername(String username) {
        return this.accessTokenManager.getAccessTokenByUsername(username);
    }


    public String grantAppToken(String appid) {

        String accessToken = this.appAccessTokenManager.generator();
        appAccessTokenManager.buildTokenAppRelation(accessToken, appid);

        return accessToken;
    }


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
