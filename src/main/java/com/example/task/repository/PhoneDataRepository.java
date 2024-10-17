package com.example.task.repository;

import com.example.task.entities.EmailData;
import com.example.task.entities.PhoneData;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PhoneDataRepository extends JpaRepository<PhoneData, Long> {
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"user"})
    Optional<PhoneData> findById(Long id);
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"user"})
    Optional<EmailData> findByPhone(String phone);
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"user"})
    boolean existsByPhoneAndUser(String phone, Long userId);


}