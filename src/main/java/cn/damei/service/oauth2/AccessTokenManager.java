package cn.damei.service.oauth2;

import com.google.common.collect.Maps;
import cn.damei.entity.auth.User;
import cn.damei.utils.UUIDGenerator;
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


    private static final String TIMESTAMP = "timestamp";


    public String generator() {
        return UUIDGenerator.generateValue();
    }


    public void buildTokenUserRelation(String accessToken, User user) {
        if (StringUtils.isNotBlank(accessToken) && user != null) {
            String key = this.buildKey(tokenPrefix, accessToken);
            Map<String, String> userMap = this.buildUserAsMap(user);
            this.setHashToRedis(key, userMap, expireTime, TimeUnit.MILLISECONDS);
        }
    }


    public void relieveTokenUserRelation(String accessToken) {
        if (StringUtils.isNotEmpty(accessToken)) {
            String key = this.buildKey(tokenPrefix, accessToken);
            redisTemplate.delete(key);
        }

    }


    public void buildUserTokenRelation(User user, String accessToken) {
        if (user != null && StringUtils.isNotBlank(accessToken)) {
            String key = this.buildKey(usernameTokenPrefix, user.getUsername());
            this.stringRedisTemplate.opsForValue().set(key, accessToken, expireTime, TimeUnit.MILLISECONDS);
        }
    }


    public void relieveUserTokenRelation(String username) {
        if (StringUtils.isNotEmpty(username)) {
            String key = this.buildKey(usernameTokenPrefix, username);
            this.stringRedisTemplate.delete(key);
        }
    }


    public String getAccessTokenByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            return StringUtils.EMPTY;
        }
        return this.stringRedisTemplate.opsForValue().get(this.buildKey(usernameTokenPrefix, username));
    }


    private Map<String, String> buildUserAsMap(User user) {
        Map<String, String> userMap = Maps.newHashMap();
        userMap.put("id", String.valueOf(user.getId()));
        userMap.put("username", user.getUsername());
        userMap.put(TIMESTAMP, String.valueOf(System.currentTimeMillis()));
        return userMap;
    }


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


    private String buildKey(String prefix, String accessToken) {
        return prefix + accessToken;
    }

}
