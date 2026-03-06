package com.whatslovermbti.mbti_prj.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCache kakaoCandidateCache =
                new CaffeineCache(
                        "kakaoCandidateCache",
                        Caffeine.newBuilder()
                                .expireAfterWrite(6, TimeUnit.HOURS) // TTL
                                .maximumSize(200)
                                .build()
                );

        CaffeineCache kakaoPlaceCache =
                new CaffeineCache(
                        "kakaoPlaceCache",
                        Caffeine.newBuilder()
                                .expireAfterWrite(24, TimeUnit.HOURS)
                                .maximumSize(200)
                                .build()
                );


        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(List.of(
                kakaoPlaceCache,
                kakaoCandidateCache
        ));

        return manager;
    }
}