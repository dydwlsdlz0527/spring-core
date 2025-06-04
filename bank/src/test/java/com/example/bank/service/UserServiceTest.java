package com.example.bank.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.bank.config.dummy.DummyObject;
import com.example.bank.domain.user.User;
import com.example.bank.domain.user.UserRepository;
import com.example.bank.dto.user.UserRequestDto.SignUpReqDto;
import com.example.bank.dto.user.UserResponseDto.SignUpResDto;

// Spring 관련 Bean들이 하나도 없는 환경!!
@ExtendWith(MockitoExtension.class)
public class UserServiceTest extends DummyObject{

    @InjectMocks
    private UserService userSerivce;

    @Mock // 가짜로 UserRepository를 띄워서 UserSerivce에 주입해준다.
    private UserRepository userRepository;

    @Spy // 진짜로 꺼내서 UserSerivce에 주입해준다.
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void signUp_test() throws Exception {
        // given
        SignUpReqDto reqDto = new SignUpReqDto();
        reqDto.setUsername("test");
        reqDto.setPassword("1234");
        reqDto.setEmail("test@test.com");
        reqDto.setFullname("kimyongjin");

        // 1. stub : 가정법, 가설
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        //when(userRepository.findByUsername(any())).thenReturn(Optional.of(new User()));
        
        // 2. stub
        User user = newMockUser(1L, "test", "kimyongjin");
        when(userRepository.save(any())).thenReturn(user);

        // when
        SignUpResDto resDto = userSerivce.signUp(reqDto);
        System.out.println("테스트 : " + resDto);

        // then
        assertThat(resDto.getId()).isEqualTo(1L);
        assertThat(resDto.getUsername()).isEqualTo("test");

    }
}
