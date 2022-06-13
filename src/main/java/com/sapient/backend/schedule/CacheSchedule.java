package com.sapient.backend.schedule;

import com.sapient.backend.config.CacheConfig;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CacheSchedule {

    private static final String CACHE_TO_EVICT = "weatherData";
    private static final int CACHE_REFRESH_RATE = 600000 * 3;

    @Scheduled(fixedRate = CACHE_REFRESH_RATE)
    public void evictWeatherPredictionCache() {
        CacheConfig.evictCache(CACHE_TO_EVICT);
    }
}
