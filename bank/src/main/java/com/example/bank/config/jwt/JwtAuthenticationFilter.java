package com.example.bank.config.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.bank.config.auth.LoginUser;
import com.example.bank.dto.user.UserRequestDto.LoginReqDto;
import com.example.bank.dto.user.UserResponseDto.LoginRespDto;
import com.example.bank.utils.CustomResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    private final Logger log = LoggerFactory.getLogger(getClass());

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        setFilterProcessesUrl("/api/login");
        this.authenticationManager = authenticationManager;
    }

    // Post : /api/login
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        log.debug("디버그 : attemptAuthentication 시작");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            LoginReqDto loginReqDto = objectMapper.readValue(request.getInputStream(), LoginReqDto.class);
            // 강제 로그인
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginReqDto.getUsername(), loginReqDto.getPassword());

            // UserDetailsService의 loadUserByUsername 호출
            // JWT를 쓴다 하더라도, 컨트롤러 진입을 하면 시큐리티의 권한 체크, 인증 체크의 도움을 받을 수 있게 세션을 만든다.
            // 이 세션의 유효 기간은 request하고, response하면 끝
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            return authentication;
        } catch (Exception e) {
            // unsuccessfulAuthentication 호출함.
            throw new InternalAuthenticationServiceException(e.getMessage());
        }
    }

    // return authentication 잘 작동하면 successfulAuthentication 메서드가 호출됩니다.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        log.debug("디버그 : successfulAuthentication 시작");
        // 1. 세션에 있는 UserDetails 가져오기
        LoginUser loginUser = (LoginUser) authResult.getPrincipal();
        // 2. 세션값으로 토큰 생성
        String jwtToken = JwtProcess.createToken(loginUser);
        // 3. 토큰을 헤더에 담기
        response.addHeader(JwtVO.HEADER, jwtToken);
        // 4. 토큰 담아서 성공 응답하기
        LoginRespDto loginResDto = new LoginRespDto(loginUser.getUser());
        CustomResponseUtil.success(response, loginResDto);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
                CustomResponseUtil.unAuthentication(response, "로그인 실패");
    }

    

}
