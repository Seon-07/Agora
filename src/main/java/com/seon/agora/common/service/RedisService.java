package com.seon.agora.common.service;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author seonjihwan
 * @version 1.0
 * @since 2025-07-27
 */
@Service
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ValueOperations<String, String> valueOperations;
    private final ListOperations<String, String> listOperations;

    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
        this.listOperations = redisTemplate.opsForList();
    }

    public String get(String key) {
        return valueOperations.get(key);
    }

    public void set(String key, String value) {
        valueOperations.set(key, value);
    }

    public void set(String key, String value, long timeout, TimeUnit unit) {
        valueOperations.set(key, value, timeout, unit);
    }

    // 리스트에 값 추가
    public void add(String key, String value) {
        listOperations.rightPush(key, value);
    }

    // 리스트에서 값 가져오기
    public List<String> getList(String key, long start, long end) {
        return listOperations.range(key, start, end);
    }

    //리스트에서 해당값 전체 삭제하기
    public void removeAll(String key, String value) {
        listOperations.remove(key, 0, value);
    }

    //리스트에서 해당 값 삭제
    public void remove(String key, String value) {
        listOperations.remove(key, 1, value);
    }

    public boolean delete(String key) {
        return redisTemplate.delete(key);
    }
}
