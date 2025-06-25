package com.example.service.util;


import com.alibaba.fastjson2.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
public class CacheRefreshService {
    @Autowired
    private RedisUtils redisUtils;

    /**
     * @param cacheKey redis缓存key
     * @param typeRef 反序列化为指定类
     * @param dbQueryFunc 数据库的调查方法
     * @param cacheExpireSeconds 缓存数据过期时间（暂时不用)
     * @param lockExpireSeconds 锁过期时间
     * @param sendRefreshMessage 发送刷新缓存消息的函数
     * @return
     * @param <T>
     */
    public <T> T cacheWithAsyncRefresh(
            String cacheKey,
            TypeReference<T> typeRef,  // ✅ 使用 TypeReference 替代 Class<T>
            Supplier<T> dbQueryFunc,
            long cacheExpireSeconds ,
            long lockExpireSeconds ,
            Consumer<String> sendRefreshMessage
    ) {
        // 1. 先从Redis拿缓存
        if (redisUtils.exists(cacheKey)) {
            T cached = redisUtils.getObject(cacheKey, typeRef);  // ✅ 使用泛型解析
            if (cached != null) {
                return cached;
            }
        }

        // 2. 尝试加锁
        String lockKey = "lock:" + cacheKey;
        if (redisUtils.tryLock(lockKey, lockExpireSeconds)) {
            try {
                sendRefreshMessage.accept(cacheKey);
            } finally {
                redisUtils.unlock(lockKey);
            }
        }

        // 3. 最后 fallback 查询数据库（避免等待 MQ 处理）
        return dbQueryFunc.get();
    }


}
