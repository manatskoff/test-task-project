package com.example.task.repository;

import com.example.task.entities.EmailData;
import com.example.task.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface EmailDataRepository extends JpaRepository<EmailData, Long> {
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"user"})
    Optional<EmailData> findById(Long id);
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"user"})
    Optional<EmailData> findByEmail(String email);
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"user"})
    boolean existsByEmailAndUser(String email, Long userId);

}