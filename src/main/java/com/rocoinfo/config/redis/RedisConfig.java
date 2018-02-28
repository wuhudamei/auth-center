package com.rocoinfo.config.redis;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * <dl>
 * <dd>Description: </dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/6 下午5:12</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    @Bean
    public <K, V> RedisTemplate<K, V> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<K, V> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        // 设置K的序列化器，默认的序列化器在hash结构中key会出现乱码
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);

        return redisTemplate;
    }
}
