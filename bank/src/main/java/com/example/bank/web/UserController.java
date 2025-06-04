package com.example.bank.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bank.dto.ResponseDto;
import com.example.bank.dto.user.UserRequestDto.SignUpReqDto;
import com.example.bank.dto.user.UserResponseDto.SignUpResDto;
import com.example.bank.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class UserController {
    
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignUpReqDto signUpReqDto, BindingResult bindingResult) {

        SignUpResDto signUpResDto = userService.signUp(signUpReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1,"회원 가입 성공",signUpResDto), HttpStatus.CREATED);
    }
    
}
