package com.gljr.jifen.service;

import com.gljr.jifen.common.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private final static Logger LOG = LoggerFactory.getLogger(RedisService.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    public boolean hasKey(String key) {
        return this.redisTemplate.hasKey(key);
    }

    public void evict(String... keys) {
        if (null != keys && keys.length > 0) {
            if (keys.length == 1) {
                this.redisTemplate.delete(keys[0]);
            } else {
                this.redisTemplate.delete(CollectionUtils.arrayToList(keys));
            }
        }
    }

    public void put(String key, String value) {
        this.put(key, value, 3600, TimeUnit.SECONDS);
    }

    public void put(String key, String value, long timeout, TimeUnit timeUnit) {
        this.redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    public String get(String key) {
        return this.redisTemplate.opsForValue().get(key);
    }

    public <T> Map<String, T> getMap(String key, Class<T> clazz) {
        String value = this.get(key);
        if (null != value) {
            try {
                return JsonUtil.jsonToMap(value, clazz);
            } catch (Exception ex) {
                LOG.error(ex.getMessage(), ex);
            }
        }
        return null;
    }

}
