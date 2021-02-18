package com.renfa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.renfa.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
