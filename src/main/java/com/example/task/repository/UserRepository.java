package com.example.task.repository;


import com.example.task.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select e from User e")
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"account"})
    List<User> findAllWithAccount();
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"emailData"})
    boolean existsByEmailDataEmail(String email);
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"phoneData"})
    boolean existsByPhoneDataPhone(String email);
    Page<User> findByDateOfBirthAfter(LocalDate dateOfBirth, Pageable pageable);
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"phoneData"})
    Page<User> findByPhoneData_Phone(String phone, Pageable pageable);
    Page<User> findByUsernameStartingWith(String name, Pageable pageable);
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"emailData"})
    Page<User> findByEmailData_Email(String email, Pageable pageable);

}