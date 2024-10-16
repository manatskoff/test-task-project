package com.example.task.schedul;

import com.example.task.entities.User;
import com.example.task.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BalanceScheduler {

    @Autowired
    private UserService userService;

    @Scheduled(fixedRate = 30000) // Каждые 30 секунд
    public void increaseBalances() {
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            userService.increaseBalance(user);
        }
    }
}