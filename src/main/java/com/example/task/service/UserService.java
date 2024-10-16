package com.example.task.service;

import com.example.task.entities.EmailData;
import com.example.task.entities.PhoneData;
import com.example.task.entities.Role;
import com.example.task.entities.User;
import com.example.task.repository.EmailDataRepository;
import com.example.task.repository.PhoneDataRepository;
import com.example.task.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import java.time.LocalDate;
import java.util.List;

import static org.slf4j.LoggerFactory.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger logger = getLogger(UserService.class);


    private final UserRepository repository;
    private final EmailDataRepository emailDataRepository;
    private final PhoneDataRepository phoneDataRepository;

    public User save(User user) {
        return repository.save(user);
    }

    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("User exist");
        }
        if (user.getEmailData() != null && !user.getEmailData().isEmpty() && repository.existsByEmailDataEmail(user.getEmailData().get(0).getEmail())) {
            throw new RuntimeException("User exist");
        }
        if (user.getPhoneData() != null && !user.getPhoneData().isEmpty() && repository.existsByPhoneDataPhone(user.getPhoneData().get(0).getPhone())) {
            throw new RuntimeException("User exist");
        }
        return save(user);
    }

    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public void increaseBalance(User user) {
        if (user.getAccount() != null && user.getAccount().getBalance() != null && user.getAccount().getInitialBalance() != null) {
            BigDecimal currentBalance = user.getAccount().getBalance();
            BigDecimal initialBalance = user.getAccount().getInitialBalance();

            BigDecimal maxBalance = initialBalance.multiply(BigDecimal.valueOf(2.07)); // Максимальный предел
            BigDecimal newBalance = currentBalance.multiply(BigDecimal.valueOf(1.10)); // Увеличение баланса на 10%
            if (newBalance.compareTo(maxBalance) > 0)
                newBalance = maxBalance;

            user.getAccount().setBalance(newBalance);
            repository.save(user);
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
    public void transferMoney(Long fromUserId, Long toUserId, BigDecimal amount) {
        logger.info("Start transfer money from {} - to {}", fromUserId, toUserId);

        User fromUser = repository.findById(fromUserId)
                .orElseThrow(() -> new RuntimeException("User From not found"));
        User toUser = repository.findById(toUserId)
                .orElseThrow(() -> new RuntimeException("User To not found"));

        BigDecimal currentBalance = fromUser.getAccount().getBalance();
        if (currentBalance.compareTo(amount) < 0) {
            logger.error("Error currentBalance");
            throw new IllegalArgumentException("Error currentBalance");
        }
        fromUser.getAccount().setBalance(currentBalance.subtract(amount));
        BigDecimal recipientBalance = toUser.getAccount().getBalance();
        toUser.getAccount().setBalance(recipientBalance.add(amount));
        repository.save(fromUser);
        repository.save(toUser);
        logger.info("Successed transfer");

    }

    public void addEmail(Long userId, String newEmail) {
        User user = repository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if (emailDataRepository.existsByEmailAndUser(newEmail, user.getId()))
            throw new RuntimeException("Email already in use");

        EmailData emailData = new EmailData();
        emailData.setEmail(newEmail);
        emailData.setUser(user);
        user.getEmailData().add(emailData);
        repository.save(user);
    }

    public void updateEmail(Long emailId, String newEmail) {
        EmailData email = emailDataRepository.findById(emailId).orElseThrow(() -> new RuntimeException("email not found"));
        email.setEmail(newEmail);
        emailDataRepository.save(email);
    }

    public void addPhone(Long userId, String newPhone) {
        User user = repository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if (phoneDataRepository.existsByPhoneAndUser(newPhone, user.getId()))
            throw new RuntimeException("Phone already in use");

        PhoneData phoneData = new PhoneData();
        phoneData.setPhone(newPhone);
        phoneData.setUser(user);
        user.getPhoneData().add(phoneData);
        repository.save(user);
    }

    public void updatePhone(Long phoneId, String phoneNumber) {
        PhoneData phone = phoneDataRepository.findById(phoneId).orElseThrow(() -> new RuntimeException("email not found"));
        phone.setPhone(phoneNumber);
        phoneDataRepository.save(phone);
    }

    public void deleteEmail(Long userId, Long emailId) {
        User user = repository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        EmailData emailData = emailDataRepository.findById(emailId).orElseThrow(() -> new RuntimeException("Email not found"));

        user.getEmailData().remove(emailData);
        emailDataRepository.delete(emailData);
        repository.save(user);
    }

    public void deletePhone(Long userId, Long phoneId) {
        User user = repository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        PhoneData phoneData = phoneDataRepository.findById(phoneId).orElseThrow(() -> new RuntimeException("Phone not found"));

        user.getPhoneData().remove(phoneData);
        phoneDataRepository.delete(phoneData);
        repository.save(user);
    }


    public Page<User> searchUsers(LocalDate dateOfBirth, String phone, String name, String email, Pageable pageable) {
        if (dateOfBirth != null) {
            return repository.findByDateOfBirthAfter(dateOfBirth, pageable);
        }
        if (phone != null) {
            return repository.findByPhoneData_Phone(phone, pageable);
        }
        if (name != null) {
            return repository.findByUsernameStartingWith(name, pageable);
        }
        if (email != null) {
            return repository.findByEmailData_Email(email, pageable);
        }
        return repository.findAll(pageable);
    }


    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }
    /**
     * Получение текущего пользователя
     */
    public User getCurrentUser() {
        // Получение имени пользователя из контекста Spring Security
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }
    /**
     * Выдача прав администратора текущему пользователю
     */
    @Deprecated
    public void getAdmin() {
        var user = getCurrentUser();
        user.setRole(Role.ROLE_ADMIN);
        save(user);
    }
}