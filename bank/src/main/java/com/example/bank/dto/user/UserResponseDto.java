package com.example.bank.dto.user;

import com.example.bank.domain.user.User;
import com.example.bank.utils.CustomDateUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class UserResponseDto {

    @Getter
    @Setter
    public static class LoginRespDto {
        private Long id;
        private String username;
        private String createdAt;

        public LoginRespDto(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.createdAt = CustomDateUtil.toStringFormat(user.getCreatedAt());
        }

        
    }

    @ToString
    @Getter
    @Setter
    public static class SignUpResDto {
        private Long id;
        private String username;
        private String fullname;

        public SignUpResDto(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.fullname = user.getFullname();
        }
    }
}
