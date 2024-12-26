package com.example.loginregister.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CacheStatisticsService {

	private static final Logger logger = LoggerFactory.getLogger(SellerService.class);

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	public void printCacheStats() {
		Cache cache = cacheManager.getCache("myDataCache");
		if (cache != null) {
			logger.info("Cache name: " + cache.getName());
			logger.info("Cache size: " + getCacheSize(cache));
		}
	}

	private long getCacheSize(Cache cache) {
		if (cache.getNativeCache() instanceof RedisCache) {
			RedisCache redisCache = (RedisCache) cache.getNativeCache();
			return redisTemplate.keys(redisCache.getName() + "*").size();
		}
		return 0;
	}
}
