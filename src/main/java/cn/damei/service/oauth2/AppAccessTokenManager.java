package cn.damei.service.oauth2;

import cn.damei.utils.UUIDGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class AppAccessTokenManager {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${oauth2.app.token.expire.time}")
    private long expireTime;
    @Value("${oauth2.app.token.redis.prefix}")
    private String appTokenPrefix;


    protected String generator() {
        return UUIDGenerator.generateValue();
    }


    public void buildTokenAppRelation(String accessToken,String appid) {
        if (StringUtils.isNoneBlank(accessToken,appid)) {
            String key = this.buildKey(appTokenPrefix, accessToken);
            this.stringRedisTemplate.opsForValue().set(key, appid, expireTime, TimeUnit.MILLISECONDS);
        }
    }


    public String getAppidByAccessToken(String accessToken) {
        if (StringUtils.isBlank(accessToken)) {
            return StringUtils.EMPTY;
        }
        return this.stringRedisTemplate.opsForValue().get(this.buildKey(appTokenPrefix, accessToken));
    }


    private String buildKey(String appTokenPrefix, String accessToken) {
        return appTokenPrefix + accessToken;
    }


}
