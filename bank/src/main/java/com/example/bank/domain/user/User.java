package com.example.bank.domain.user;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.bank.handler.ex.CustomApiException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor // 스프링이 User 객체 생성 시 빈 생성자로 new를 하기 때문
@Getter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_tb")
@Entity
public class User { // extends 시간 설정 (상속)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String username;

    @Column(nullable = false, length = 60)
    private String password; // 패스워드 인코딩(BCRYPT)

    @Column(nullable = false, length = 20)
    private String email;

    @Column(nullable = false, length = 20)
    private String fullname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserEnum role; // ADMIN, CUSTOMER

    @CreatedDate // Insert 시 자동으로 생성됨
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate // Insert, Upadte 시 자동으로 생성됨
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public User(Long id, String username, String password, String email, String fullname, UserEnum role,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullname = fullname;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

    }

    public void authenticationPassword(String currentPassword, BCryptPasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(currentPassword, this.password)) {
            throw new CustomApiException("패스워드 인증에 실패하였습니다");
        }
    }

    public void checkSamePassword(String newPassword, BCryptPasswordEncoder passwordEncoder) {
        if (passwordEncoder.matches(newPassword, this.password)) {
            throw new CustomApiException("새로운 패스워드가 현재 패스워드와 동일합니다");
        }
    }

    public void updatePassword(String currentPassword, String newPassword,
            BCryptPasswordEncoder passwordEncoder) {
        authenticationPassword(currentPassword, passwordEncoder);
        checkSamePassword(newPassword, passwordEncoder);
        this.password = passwordEncoder.encode(newPassword);
    }

}
