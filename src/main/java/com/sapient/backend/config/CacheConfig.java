package com.sapient.backend.config;


import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.util.Arrays.asList;

@Component
@EnableCaching
public class CacheConfig implements CacheManagerCustomizer<ConcurrentMapCacheManager> {

    private static final String[] REGISTERED_CACHES = new String[]{"weatherData"};
    private static ConcurrentMapCacheManager cacheManager;

    private CacheConfig() {
    }

    public static void evictCache(String cacheName) {
        Optional<Cache> cache = Optional.ofNullable(cacheManager.getCache(cacheName));
        cache.ifPresent(Cache::clear);
    }

    @Override
    public void customize(ConcurrentMapCacheManager concurrentMapCacheManager) {
        cacheManager = concurrentMapCacheManager;
        cacheManager.setCacheNames(asList(REGISTERED_CACHES));
    }
}
