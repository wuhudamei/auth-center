package cn.damei.config;

import com.google.common.collect.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;
import java.util.concurrent.TimeUnit;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testSet() {
        redisTemplate.opsForValue().set("hello", "world");
        String result = redisTemplate.opsForValue().get("hello");
        System.out.println(result);
    }

    @Test
    public void testHashSet() {
        String key = "oauth2:token:1e03a6e747453a12b6065094a0dfbfef";
        Map<String, String> value = Maps.newHashMap();
        value.put("id", "1");
        value.put("username", "test");
        value.put("name", "张三");
        value.put("mobile", "123333333");
        value.put("email", "test@damei.com");

        this.setHashToRedis(key, value, 10000000L, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testHashGet() {
        HashOperations<String, String, Object> hashCache = redisTemplate.opsForHash();
        Object value = hashCache.get("oauth2:token:6bf26b2c95a63bcc8356c90f719d7695", "name");
        System.out.println(value);
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
}
