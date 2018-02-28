package com.rocoinfo.service.oauth2;

import com.google.common.collect.Maps;
import com.rocoinfo.entity.auth.User;
import com.rocoinfo.utils.UUIDGenerator;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <dl>
 * <dd>Description: 用于缓存和获取accessToken</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/5 13:38</dd>
 * <dd>@author：Kong</dd>
 * </dl>
 */
@Service
public class AccessTokenManager {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${oauth2.token.expire.time}")
    private long expireTime;
    @Value("${oauth2.token.redis.prefix}")
    private String tokenPrefix;
    @Value("${oauth2.username.token.redis.prefix}")
    private String usernameTokenPrefix;

    /**
     * 用于判断是否失效的字段
     */
    private static final String TIMESTAMP = "timestamp";

    /**
     * 获取唯一的AccessToken
     *
     * @return 返回AccessToken
     */
    public String generator() {
        return UUIDGenerator.generateValue();
    }

    /**
     * 建立accesstoken和用户的关联关系
     *
     * @param accessToken AccessToken
     * @param user        用户信息
     */
    public void buildTokenUserRelation(String accessToken, User user) {
        if (StringUtils.isNotBlank(accessToken) && user != null) {
            String key = this.buildKey(tokenPrefix, accessToken);
            Map<String, String> userMap = this.buildUserAsMap(user);
            this.setHashToRedis(key, userMap, expireTime, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 解除accessToken和用户的关联关系
     *
     * @param accessToken accessToken
     */
    public void relieveTokenUserRelation(String accessToken) {
        if (StringUtils.isNotEmpty(accessToken)) {
            String key = this.buildKey(tokenPrefix, accessToken);
            redisTemplate.delete(key);
        }

    }

    /**
     * 建立用户和access_token的关联关系 key:oauth2:username:token:username值  value:token值
     *
     * @param user        用户信息
     * @param accessToken AccessToken
     */
    public void buildUserTokenRelation(User user, String accessToken) {
        if (user != null && StringUtils.isNotBlank(accessToken)) {
            String key = this.buildKey(usernameTokenPrefix, user.getUsername());
            this.stringRedisTemplate.opsForValue().set(key, accessToken, expireTime, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 解除用户和AccessToken的关联关系
     *
     * @param username 用户名
     */
    public void relieveUserTokenRelation(String username) {
        if (StringUtils.isNotEmpty(username)) {
            String key = this.buildKey(usernameTokenPrefix, username);
            this.stringRedisTemplate.delete(key);
        }
    }

    /**
     * 根据用户名查询AccessToken
     *
     * @param username 用户名
     * @return
     */
    public String getAccessTokenByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            return StringUtils.EMPTY;
        }
        return this.stringRedisTemplate.opsForValue().get(this.buildKey(usernameTokenPrefix, username));
    }

    /**
     * 将用户信息构建成map
     *
     * @param user 用户信息
     * @return
     */
    private Map<String, String> buildUserAsMap(User user) {
        Map<String, String> userMap = Maps.newHashMap();
        userMap.put("id", String.valueOf(user.getId()));
        userMap.put("username", user.getUsername());
        userMap.put(TIMESTAMP, String.valueOf(System.currentTimeMillis()));
        return userMap;
    }

    /**
     * 将hash结构的数据放入redis
     *
     * @param key      key
     * @param value    value
     * @param timeout  失效时间
     * @param timeUnit 时间单位
     */
    private <HK, HV> void setHashToRedis(String key, Map<HK, HV> value, Long timeout, TimeUnit timeUnit) {
        if (key != null && value != null) {
            HashOperations<String, HK, HV> hashCache = redisTemplate.opsForHash();
            hashCache.putAll(key, value);
            if (timeout != null && timeUnit != null) {
                redisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
            }
        }
    }

    private <HK, HV> HV getHashValueFromRedis(String key, HK hashKey) {
        HashOperations<String, HK, HV> hashCache = redisTemplate.opsForHash();
        return hashCache.get(key, hashKey);
    }

    /**
     * 校验accessToken是否有效
     *
     * @param accessToken accessToken
     * @return 有效返回true，无效返回false
     */
    public boolean validate(String accessToken) {
        if (StringUtils.isEmpty(accessToken)) {
            return false;
        }
        HashOperations<String, String, String> hashCache = redisTemplate.opsForHash();
        Map<String, String> entries = hashCache.entries(buildKey(tokenPrefix, accessToken));
        if (MapUtils.isNotEmpty(entries)) {
            String createTime = entries.get(TIMESTAMP);
            if (StringUtils.isNotEmpty(createTime) &&
                    (System.currentTimeMillis() - Long.parseLong(createTime) < expireTime)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 构建accessToken的key
     *
     * @param accessToken 指定的accessToken
     * @return 添加缓存前缀，并返回
     */
    private String buildKey(String prefix, String accessToken) {
        return prefix + accessToken;
    }

}
