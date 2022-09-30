package com.nooblol.global.config;

import java.util.List;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class LocalCacheConfig {

  @Bean
  public CacheManager cacheManager() {
    SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
    simpleCacheManager.setCaches(
        List.of(new ConcurrentMapCache("category"), new ConcurrentMapCache("bbs"),
            new ConcurrentMapCache("allBbs")));
    return simpleCacheManager;
  }
}
