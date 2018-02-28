package com.rocoinfo.service.oauth2;

import com.rocoinfo.utils.UUIDGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * <dl>
 * <dd>Description: 用于管理应用级的AccessToken:
 * 本Token是第三方应用全局唯一的AccessToken，第三方应用负责实现刷新机制</dd>
 * <dd>Company: 大诚若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/22 10:08</dd>
 * <dd>@author：Kong</dd>
 * </dl>
 */
@Service
public class AppAccessTokenManager {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${oauth2.app.token.expire.time}")
    private long expireTime;
    @Value("${oauth2.app.token.redis.prefix}")
    private String appTokenPrefix;

    /**
     * 获取唯一的AccessToken
     *
     * @return 返回AccessToken
     */
    protected String generator() {
        return UUIDGenerator.generateValue();
    }

    /**
     * 建立accessToken与应用的对应关系 key:oauth2:app:token:accessToken值  value:appid值
     *
     * @param accessToken        用户信息
     * @param appid 应用id
     */
    public void buildTokenAppRelation(String accessToken,String appid) {
        if (StringUtils.isNoneBlank(accessToken,appid)) {
            String key = this.buildKey(appTokenPrefix, accessToken);
            this.stringRedisTemplate.opsForValue().set(key, appid, expireTime, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 根据用户名查询AccessToken
     *
     * @param accessToken 应用的accessToken
     * @return 返回
     */
    public String getAppidByAccessToken(String accessToken) {
        if (StringUtils.isBlank(accessToken)) {
            return StringUtils.EMPTY;
        }
        return this.stringRedisTemplate.opsForValue().get(this.buildKey(appTokenPrefix, accessToken));
    }

    /**
     * 构建存储Token的key
     *
     * @param appTokenPrefix 存储到redis的前缀
     * @param accessToken 待缓存的accessToken
     * @return 返回redis缓存accessToken的key
     */
    private String buildKey(String appTokenPrefix, String accessToken) {
        return appTokenPrefix + accessToken;
    }


}
