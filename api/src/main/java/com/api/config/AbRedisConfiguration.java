package com.api.config;

import org.springframework.data.redis.core.StringRedisTemplate;

public class AbRedisConfiguration {
    protected StringRedisTemplate temple;

    public void setData(String key, String value) {
        getTemple().opsForValue().set(key, value);
    }

    public String getData(String key) {
        return getTemple().opsForValue().get(key);
    }

    public StringRedisTemplate getTemple() {
        return temple;
    }

}
