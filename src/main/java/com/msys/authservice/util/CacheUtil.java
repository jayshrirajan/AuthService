package com.msys.authservice.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CacheUtil {

    @Autowired
    RedisCacheManager cacheManager;

    public void evictSingleCacheValue(String cacheName, String cacheKey) {
        cacheManager.getCache(cacheName).evict(cacheKey);
    }

    public void evictAllCacheValues(String cacheName) {
        cacheManager.getCache(cacheName).clear();
    }

    public void evictAllCaches() {
        cacheManager.getCacheNames().stream()
                .forEach(cacheName -> cacheManager.getCache(cacheName).clear());
    }

}
