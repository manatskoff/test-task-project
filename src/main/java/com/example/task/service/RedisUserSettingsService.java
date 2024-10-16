package com.example.task.service;

import com.example.task.dto.RedisUserSettings;
import com.example.task.repository.RedisUserSettingsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class RedisUserSettingsService {

    private final RedisUserSettingsRepository repository;

    public void saveSettings(RedisUserSettings settings) {
        repository.saveUserSettings(settings);
    }

    public RedisUserSettings getSettings(String userId) {
        return repository.getUserSettings(userId);
    }
}