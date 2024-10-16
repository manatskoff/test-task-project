package com.example.task.repository;


import com.example.task.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmailDataEmail(String email);
    boolean existsByPhoneDataPhone(String email);
    Page<User> findByDateOfBirthAfter(LocalDate dateOfBirth, Pageable pageable);
    Page<User> findByPhoneData_Phone(String phone, Pageable pageable);
    Page<User> findByUsernameStartingWith(String name, Pageable pageable);
    Page<User> findByEmailData_Email(String email, Pageable pageable);
}