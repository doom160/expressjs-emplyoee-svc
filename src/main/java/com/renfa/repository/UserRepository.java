package com.renfa.repository;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;

import org.springframework.data.domain.Pageable;

import com.renfa.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT u FROM User u WHERE u.salary between ?1 and ?2")
    List<User> findBySalary(float minSalary, float maxSalary, Pageable pageable);

    @Query(value = "SELECT u FROM User u WHERE u.id = ?1")
    List<User> findById(String id);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM User u WHERE u.id = ?1")
    void deleteById(String id);
}
