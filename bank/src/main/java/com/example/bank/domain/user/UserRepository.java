package com.example.bank.domain.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    // save - JpaRepository 에서 자동으로 생성됨.

    // SELECT * FROM USER WHERE USERNAME = ?
    Optional<User> findByUsername(String username); // Jpa NameQuery 작동
    
}
