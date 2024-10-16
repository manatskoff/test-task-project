package com.example.task.repository;

import com.example.task.entities.EmailData;
import com.example.task.entities.PhoneData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PhoneDataRepository extends JpaRepository<PhoneData, Long> {
    Optional<PhoneData> findById(Long id);
    boolean existsByPhoneAndUser(String phone, Long userId);


}