package com.example.bank.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.bank.config.dummy.DummyObject;
import com.example.bank.domain.user.UserRepository;
import com.example.bank.dto.user.UserRequestDto.SignUpReqDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class UserControllerTest extends DummyObject{

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserRepository userRepository;


    @Test
    public void signUp_success_test() throws Exception {

        // given
        SignUpReqDto signUpReqDto = new SignUpReqDto();
        signUpReqDto.setUsername("kim");
        signUpReqDto.setPassword("1234");
        signUpReqDto.setEmail("kim@test.com");
        signUpReqDto.setFullname("kimyongjin");

        String requestBody = om.writeValueAsString(signUpReqDto);

        // when
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.post("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void signUp_fail_test() throws Exception {

        
        // given
        dataSetting();
        SignUpReqDto signUpReqDto = new SignUpReqDto();
        signUpReqDto.setUsername("testfail");
        signUpReqDto.setPassword("123456");
        signUpReqDto.setEmail("test@test.com");
        signUpReqDto.setFullname("kimyongjin");

        String requestBody = om.writeValueAsString(signUpReqDto);

        // when
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.post("/api/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    private void dataSetting() {
        userRepository.save(newUser("testfail","kimyongjin"));
    }

}
