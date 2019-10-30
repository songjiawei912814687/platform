package com.wechatsug.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * leeoohoo@gmail.com
 */
@Component
public class RedisComponent {
    //操作字符串的template，StringRedisTemplate是RedisTemplate的一个子集
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    //RedisTemplate可以进行所有的操作
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    /**
     * 存放string
     * @param key
     * @param value
     * @param time
     */
    public void set(String key,String value,Long time){
        ValueOperations<String,String> ops=this.stringRedisTemplate.opsForValue();
        boolean bExistent=this.stringRedisTemplate.hasKey(key);
        if(bExistent){
        }else{
            ops.set(key,value,time, TimeUnit.SECONDS);
        }
    }

    /**
     * 根据key 获取val
     * @param key
     * @return
     */
    public String get(String key){
        return this.stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 根据key 删除
     * @param key
     */
    public void del(String key){
        this.stringRedisTemplate.delete(key);
    }

    /**
     * 存放object
     * @param key
     * @param object
     * @param time
     */
    public void sentinelSet(String key,Object object,Long time){
        redisTemplate.opsForValue().set(key,object,time,TimeUnit.SECONDS);
    }

    /**
     * 将获得的val 转成string
     * @param key
     * @return
     */
    public String sentinelGet(String key){
        return String.valueOf(redisTemplate.opsForValue().get(key));
    }

    /**
     * key是否存在
     * @param key
     * @return
     */
    public boolean hasKey(String key){
        return stringRedisTemplate.hasKey(key);
    }

    /**
     * 放入set集合
     * @param key
     * @param set
     */
    public void opsForSet(String key,String... set){
        this.stringRedisTemplate.opsForSet().add(key,set);
    }

    /**
     * 根据key获取时间
     * @param key
     * @return
     */
    public Long getExpire(String key){
       return this.stringRedisTemplate.getExpire(key,TimeUnit.SECONDS);
    }

    /**
     * 设置key 过期时间
     * @param key
     * @param time
     */
    public void  expire(String key,Long time){
        this.stringRedisTemplate.expire(key,time,TimeUnit.SECONDS);
    }

    /**
     * 集合中是否存在
     * @param key
     * @param o
     * @return
     */
    public boolean isMember(String key,Object o){
        return this.stringRedisTemplate.opsForSet().isMember(key,o);
    }

    /**
     * 获取set集合
     * @param key
     * @return
     */
    public Set<String> members(String key){
        return this.stringRedisTemplate.opsForSet().members(key);
    }

    /**
     * val 的加减
     * @param key
     * @param val
     */
    public void increment(String key,long val){
        this.stringRedisTemplate.boundValueOps(key).increment(val);
    }
}
