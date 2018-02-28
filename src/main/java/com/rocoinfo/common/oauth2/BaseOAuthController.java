package com.rocoinfo.common.oauth2;

import com.google.common.collect.Maps;
import com.rocoinfo.aop.logger.LoggerType;
import com.rocoinfo.common.BaseController;
import com.rocoinfo.entity.auth.App;
import com.rocoinfo.log.OAuthLogger;
import com.rocoinfo.service.auth.AppService;
import com.rocoinfo.service.oauth2.OAuthService;
import com.rocoinfo.utils.ServerUtils;
import com.rocoinfo.utils.UUIDGenerator;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <dl>
 * <dd>Description: 授权认证的基类</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/5 下午1:19</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
public abstract class BaseOAuthController extends BaseController {

    protected static final String OAUTH_LOGIN_MODULE = "OAUTH_LOGIN";
    protected static final String OAUTH_LOGOUT_MODULE = "OAUTH_LOGOUT";

    @Autowired
    protected AppService appService;
    @Autowired
    protected OAuthService oAuthService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 错误页面
     */
    protected static final String ERROR_PAGE = "oauth/error";

    /**
     * 成功页面
     */
    protected static final String SUCCESS_PAGE = "oauth/success";

    /**
     * 以下是model attribute的key
     */
    protected static final String ATTRIBUTE_ERROR_KEY = "error_msg";

    /**
     * 根据appid查询app
     *
     * @param appid appid
     * @return
     */
    protected App getAppByAppid(String appid) {
        return this.appService.getByAppid(appid);
    }

    /**
     * 根据appid和secret查询app
     *
     * @param appid  appid
     * @param secret secret
     * @return
     */
    protected App getAppByAppidAndSecret(String appid, String secret) {
        return this.appService.getByAppidAndSecret(appid, secret);
    }

    /**
     * 发放授权码
     *
     * @param username 需要授权码的用户名
     * @return
     */
    protected String grantCode(String username) throws OAuthException {
        try {
            return this.oAuthService.grantCode(username);
        } catch (Exception e) {
            throw new OAuthException(e);
        }
    }

    /**
     * 构建发放授权码之后跳转的url
     *
     * @param redirect 是否重定向
     * @param url      跳转url
     * @param code     系统发放的code
     * @param state    state
     * @return 返回重定向地址
     */
    protected String buildCodeRedirectUrl(boolean redirect, String url, String code, String state) {
        StringBuilder sb = new StringBuilder();
        if (redirect) {
            sb.append("redirect:");
        }
        return sb.append(url).append("?code=").append(code).append("&state=").append(state).toString();
    }

    /**
     * 判断用户是否有对应的app权限
     *
     * @param apps 用户拥有的app权限
     * @param app  当前需要判断的app
     * @return
     */
    protected boolean hasAppPermission(List<App> apps, App app) {
        if (CollectionUtils.isEmpty(apps) || app == null) {
            return false;
        }
        return apps.stream().anyMatch((o) -> app.getId().equals(o.getId()));
    }

    protected OAuthLogger buildBaseOAuthLogger(String module, HttpServletRequest request) {
        OAuthLogger logger = new OAuthLogger(UUIDGenerator.generateValue());
        logger.setType(LoggerType.BUSINESS_LOG)
                .setModule(module)
                .setTimestamp(System.currentTimeMillis())
                .setIp(request.getRemoteAddr())
                .setServer(ServerUtils.getServerIp())
                .setBrowser(getBrowser(request));
        return logger;
    }

    /**
     * 获取当前浏览器信息
     *
     * @param request 请求
     * @return
     */
    private String getBrowser(HttpServletRequest request) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        return userAgent.getBrowser().toString();
    }

    /**
     * 记录用户请求code的日志信息：浏览器信息、ip信息、时间戳等
     *
     * @param code code
     * @param req  req
     */
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

    /**
     * 获取用户请求code时的日志信息
     *
     * @param code code
     * @return
     */
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
