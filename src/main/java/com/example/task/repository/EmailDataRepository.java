package com.example.task.repository;

import com.example.task.entities.EmailData;
import com.example.task.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface EmailDataRepository extends JpaRepository<EmailData, Long> {
    Optional<EmailData> findById(Long id);
    boolean existsByEmailAndUser(String email, Long userId);

}