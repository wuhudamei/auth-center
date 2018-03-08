package cn.damei.common.oauth2;

import cn.damei.common.BaseController;
import cn.damei.entity.auth.App;
import cn.damei.log.OAuthLogger;
import cn.damei.service.auth.AppService;
import cn.damei.service.oauth2.OAuthService;
import cn.damei.utils.ServerUtils;
import cn.damei.utils.UUIDGenerator;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public abstract class BaseOAuthController extends BaseController {

    protected static final String OAUTH_LOGIN_MODULE = "OAUTH_LOGIN";
    protected static final String OAUTH_LOGOUT_MODULE = "OAUTH_LOGOUT";

    @Autowired
    protected AppService appService;
    @Autowired
    protected OAuthService oAuthService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    protected static final String ERROR_PAGE = "oauth/error";


    protected static final String SUCCESS_PAGE = "oauth/success";


    protected static final String ATTRIBUTE_ERROR_KEY = "error_msg";


    protected App getAppByAppid(String appid) {
        return this.appService.getByAppid(appid);
    }


    protected App getAppByAppidAndSecret(String appid, String secret) {
        return this.appService.getByAppidAndSecret(appid, secret);
    }


    protected String grantCode(String username) throws OAuthException {
        try {
            return this.oAuthService.grantCode(username);
        } catch (Exception e) {
            throw new OAuthException(e);
        }
    }


    protected String buildCodeRedirectUrl(boolean redirect, String url, String code, String state) {
        StringBuilder sb = new StringBuilder();
        if (redirect) {
            sb.append("redirect:");
        }
        return sb.append(url).append("?code=").append(code).append("&state=").append(state).toString();
    }


    protected boolean hasAppPermission(List<App> apps, App app) {
        if (CollectionUtils.isEmpty(apps) || app == null) {
            return false;
        }
        return apps.stream().anyMatch((o) -> app.getId().equals(o.getId()));
    }

    protected OAuthLogger buildBaseOAuthLogger(String module, HttpServletRequest request) {
        OAuthLogger logger = new OAuthLogger(UUIDGenerator.generateValue());
        logger.setModule(module)
                .setTimestamp(System.currentTimeMillis())
                .setIp(request.getRemoteAddr())
                .setServer(ServerUtils.getServerIp())
                .setBrowser(getBrowser(request));
        return logger;
    }


    private String getBrowser(HttpServletRequest request) {
        return "";
    }


    protected void processCodeLog(String code, HttpServletRequest req) {
        Map<String, String> map = Maps.newHashMap();
        map.put("ip", req.getRemoteAddr());
        map.put("server", ServerUtils.getServerIp());
        map.put("browser", this.getBrowser(req));
        String key = "logger:code:" + code;
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        hashOps.putAll(key, map);
        redisTemplate.expire(key, 60, TimeUnit.MINUTES);
    }


    protected Map<String, String> getCodeLog(String code) {
        String key = "logger:code:" + code;
        try {
            HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
            return hashOps.entries(key);
        } finally {
            redisTemplate.delete(key);
        }
    }
}
