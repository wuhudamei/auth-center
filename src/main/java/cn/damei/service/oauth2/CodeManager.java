package cn.damei.service.oauth2;

import com.google.common.collect.Lists;
import cn.damei.utils.UUIDGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
public class CodeManager {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Value("${oauth2.code.expire.time}")
    private int expireTime;
    @Value("${oauth2.code.redis.prefix}")
    private String codePrefix;
    @Value("${oauth2.username.code.redis.prefix}")
    private String usernameCodePrefix;


    public String generate() {
        return UUIDGenerator.generateValue();
    }



    public void setToRedis(String code) {
        if (StringUtils.isNotBlank(code)) {
            String key = this.getKey(codePrefix, code);
            String value = String.valueOf(System.currentTimeMillis());
            stringRedisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.MILLISECONDS);
        }
    }


    public void buildCodeUsernameRelation(String code, String username) {
        if (StringUtils.isNoneBlank(code, username)) {
            String key = this.getKey(usernameCodePrefix, code);
            stringRedisTemplate.opsForValue().set(key, username, expireTime, TimeUnit.MILLISECONDS);
        }
    }


    public String getUsernameByCode(String code) {
        if (StringUtils.isNotBlank(code)) {
            return stringRedisTemplate.opsForValue().get(this.getKey(usernameCodePrefix, code));
        }
        return null;
    }


    public String getFromRedis(String code) {
        return stringRedisTemplate.opsForValue().get(this.getKey(codePrefix, code));
    }


    public void expireCode(String code) {
        if (StringUtils.isNotBlank(code)) {
            List<String> keys = Lists.newArrayList(this.getKey(codePrefix, code), this.getKey(usernameCodePrefix, code));
            stringRedisTemplate.delete(keys);
        }
    }



    public boolean validate(String code) {
        if (StringUtils.isBlank(code)) {
            return false;
        }
        String value = this.getFromRedis(code);
        if (StringUtils.isNotBlank(value)
                && (System.currentTimeMillis() - Long.valueOf(value)) < expireTime) {
            return true;
        }
        return false;
    }


    private String getKey(String prefix, String code) {
        if (StringUtils.isNoneBlank(prefix, code)) {
            return prefix + code;
        }
        return StringUtils.EMPTY;
    }
}
