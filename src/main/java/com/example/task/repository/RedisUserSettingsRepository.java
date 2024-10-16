package com.example.task.repository;

import com.example.task.dto.RedisUserSettings;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class RedisUserSettingsRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public void saveUserSettings(RedisUserSettings settings) {
        redisTemplate.opsForValue().set(settings.getUserId(), settings);
    }

    public RedisUserSettings getUserSettings(String userId) {
        return (RedisUserSettings) redisTemplate.opsForValue().get(userId);
    }
}