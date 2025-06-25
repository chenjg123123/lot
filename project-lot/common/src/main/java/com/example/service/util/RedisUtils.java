package com.example.service.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {

    @Autowired
    private StringRedisTemplate redisTemplate;

    // ========== 字符串缓存 ==========

    public void set(String key, String value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    // ========== 对象缓存（JSON序列化） ==========

    public void setObject(String key, Object obj, long timeout, TimeUnit unit) {
        String json = JSON.toJSONString(obj);
        redisTemplate.opsForValue().set(key, json, timeout, unit);
    }

    public <T> T getObject(String key, TypeReference<T> typeRef) {
        String json = redisTemplate.opsForValue().get(key);
        if (json == null) return null;
        return JSON.parseObject(json, typeRef.getType());
    }


    // ========== 分布式锁 ==========

    public boolean tryLock(String key, long timeoutSeconds) {
        Boolean success = redisTemplate.opsForValue().setIfAbsent(key, "1", timeoutSeconds, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(success);
    }

    public void unlock(String key) {
        redisTemplate.delete(key);
    }
}

