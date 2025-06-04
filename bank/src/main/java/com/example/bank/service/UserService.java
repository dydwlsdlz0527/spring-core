package com.example.bank.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bank.domain.user.User;
import com.example.bank.domain.user.UserRepository;
import com.example.bank.handler.ex.CustomApiException;
import com.example.bank.dto.user.UserRequestDto.SignUpReqDto;
import com.example.bank.dto.user.UserResponseDto.SignUpResDto;

import lombok.RequiredArgsConstructor;

/*
 * @Transactional 어노테이션이 없으면
 * DB에 요청 후 응답 받으면 db session 사라짐(OSIV=false 일 경우)
 * 서비스 레이어에서 Lazy Loading을 할 수 없게됨.
 */
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // 서비스는 DTO를 요청받고, DTO로 응답한다.
    @Transactional // 트랜잭션이 메서드 시작할 때 시작되고 종료될 때 함께 종료.
    public SignUpResDto signUp(SignUpReqDto reqDto) {
        // 1. 동일 유저명이 존재하는지 검사
        Optional<User> user = userRepository.findByUsername(reqDto.getUsername());
        if (user.isPresent()) {
            // 유저 이름이 중복되는 경우
            throw new CustomApiException("이미 존재하는 유저 이름입니다.");
        }
        // 2. 패스워드 인코딩 + DB 저장
        User userPS = userRepository.save(reqDto.toEntity(passwordEncoder));

        // 3. DTO 응답
        return new SignUpResDto(userPS);
    }

}
