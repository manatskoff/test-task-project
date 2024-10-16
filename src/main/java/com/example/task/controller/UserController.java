package com.example.task.controller;

import com.example.task.entities.User;
import com.example.task.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    // Функция перевода денег
    @PostMapping("/transfer")
    public ResponseEntity<String> transferMoney(
            @RequestParam Long toUserId,
            @RequestParam BigDecimal amount) {

        var currentUser = userService.getCurrentUser();
        userService.transferMoney(currentUser.getId(), toUserId, amount);

        return ResponseEntity.ok("Transfer successful");
    }

    @GetMapping("/search")
    public ResponseEntity<Page<User>> searchUsers(
            @RequestParam(required = false) LocalDate dateOfBirth,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userService.searchUsers(dateOfBirth, phone, name, email, pageable);
        return ResponseEntity.ok(userPage);
    }

    @PutMapping("/{id}/addEmail")
    public ResponseEntity<String> addEmail(@PathVariable Long id, @RequestParam String email) {
        userService.addEmail(id, email);
        return ResponseEntity.ok("Email updated successfully");
    }

    @PutMapping("/updateEmail")
    public ResponseEntity<String> updateEmail(@RequestParam Long emailId, @RequestParam String email) {
        userService.updateEmail(emailId, email);
        return ResponseEntity.ok("Email updated successfully");
    }

    @PutMapping("/{id}/addPhone")
    public ResponseEntity<String> addPhone(@PathVariable Long id, @RequestParam String phone) {
        userService.addPhone(id, phone);
        return ResponseEntity.ok("Email updated successfully");
    }

    @PutMapping("/updatePhone")
    public ResponseEntity<String> updatePhone(@RequestParam Long phoneId, @RequestParam String phone) {
        userService.updatePhone(phoneId, phone);
        return ResponseEntity.ok("Phone updated successfully");
    }

    @DeleteMapping("/{id}/email/{emailId}")
    public ResponseEntity<String> deleteEmail(@PathVariable Long id, @PathVariable Long emailId) {
        userService.deleteEmail(id, emailId);
        return ResponseEntity.ok("Email deleted successfully");
    }

    @DeleteMapping("/{id}/phone/{phoneId}")
    public ResponseEntity<String> deletePhone(@PathVariable Long id, @PathVariable Long phoneId) {
        userService.deletePhone(id, phoneId);
        return ResponseEntity.ok("Phone deleted successfully");
    }


}