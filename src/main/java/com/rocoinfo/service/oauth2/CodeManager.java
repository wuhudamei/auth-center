package com.rocoinfo.service.oauth2;

import com.google.common.collect.Lists;
import com.rocoinfo.utils.UUIDGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <dl>
 * <dd>Description: 授权码管理器</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/5 上午11:19</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
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

    /**
     * 生成授权码
     *
     * @return
     */
    public String generate() {
        return UUIDGenerator.generateValue();
    }


    /**
     * 将授权码存入redis中，key为：oauth:code:code值,  value为：当前时间戳
     *
     * @param code 授权码
     * @return
     */
    public void setToRedis(String code) {
        if (StringUtils.isNotBlank(code)) {
            String key = this.getKey(codePrefix, code);
            String value = String.valueOf(System.currentTimeMillis());
            stringRedisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 建立code和username的关联关系，存到redis中
     * key: oauth2:username:code:code值
     * value: 用户名
     *
     * @param code     code
     * @param username username
     * @return
     */
    public void buildCodeUsernameRelation(String code, String username) {
        if (StringUtils.isNoneBlank(code, username)) {
            String key = this.getKey(usernameCodePrefix, code);
            stringRedisTemplate.opsForValue().set(key, username, expireTime, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 根据code查询对应的用户名
     *
     * @param code code
     * @return
     */
    public String getUsernameByCode(String code) {
        if (StringUtils.isNotBlank(code)) {
            return stringRedisTemplate.opsForValue().get(this.getKey(usernameCodePrefix, code));
        }
        return null;
    }

    /**
     * 根据key取出value，key为：oauth:code:code值 ，value实际上是key存入的时间戳
     *
     * @param code 授权码的值
     * @return
     */
    public String getFromRedis(String code) {
        return stringRedisTemplate.opsForValue().get(this.getKey(codePrefix, code));
    }

    /**
     * 使code失效
     *
     * @param code code
     */
    public void expireCode(String code) {
        if (StringUtils.isNotBlank(code)) {
            List<String> keys = Lists.newArrayList(this.getKey(codePrefix, code), this.getKey(usernameCodePrefix, code));
            stringRedisTemplate.delete(keys);
        }
    }


    /**
     * 校验授权码是否有效
     *
     * @param code 授权码
     * @return
     */
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

    /**
     * redis中key为：oauth:code:code值
     *
     * @param code code
     * @return
     */
    private String getKey(String prefix, String code) {
        if (StringUtils.isNoneBlank(prefix, code)) {
            return prefix + code;
        }
        return StringUtils.EMPTY;
    }
}
