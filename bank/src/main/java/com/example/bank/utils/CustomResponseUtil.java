package com.example.bank.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.example.bank.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

public class CustomResponseUtil {

    private final static Logger log = LoggerFactory.getLogger(CustomResponseUtil.class);

    public static void success(HttpServletResponse response, Object dto) {
        ObjectMapper mapper = new ObjectMapper();
        ResponseDto<?> responseDto = new ResponseDto<>(1, "로그인 성공", dto);
        String responseBody;
        try {
            responseBody = mapper.writeValueAsString(responseDto);
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().println(responseBody);
        } catch (Exception e) {
            log.error("서버 파싱 에러");
        }
    }

    public static void unAuthentication(HttpServletResponse response, String message) {
        ObjectMapper mapper = new ObjectMapper();
        ResponseDto<?> responseDto = new ResponseDto<>(-1, message, null);
        String responseBody;
        try {
            responseBody = mapper.writeValueAsString(responseDto);
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().println(responseBody);
        } catch (Exception e) {
            log.error("서버 파싱 에러");
        }
    }



}
