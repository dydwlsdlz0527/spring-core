package com.example.bank.dto.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.bank.domain.user.User;
import com.example.bank.domain.user.UserEnum;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class UserRequestDto {

    @Getter
    @Setter
    public static class LoginReqDto {
        private String username;
        private String password;
    }

    @Getter
    @Setter
    public static class SignUpReqDto {

        // 유효성 검사
        // 영문, 숫자는 되고, 길이 최소 2~20자 이내
        @Pattern(regexp = "^[a-zA-Z0-9]{2,20}$", message = "영문/숫자 2~20자 이내로 작성해 주세요.")
        @NotEmpty
        private String username;

        // 길이 4~20
        @Size(min = 4, max = 20)
        @NotEmpty
        private String password;

        // 이메일 형식
        @Pattern(regexp = "^[a-zA-Z0-9]{2,10}+@[a-zA-Z0-9]{2,6}\\.[a-zA-Z]{2,3}$", message = "이메일 형식이 올바르지 않습니다.")
        @NotEmpty
        private String email;

        // 영어, 한글, 1~20
        @Pattern(regexp = "^[a-zA-Z가-힣]{1,20}$", message = "영어/한글/1~20자 이내로 작성해 주세요.")
        @NotEmpty
        private String fullname;

        public User toEntity(BCryptPasswordEncoder passwordEncoder) {
            return User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .email(email)
                    .fullname(fullname)
                    .role(UserEnum.ADMIN)
                    .build();
        }
    }

}
