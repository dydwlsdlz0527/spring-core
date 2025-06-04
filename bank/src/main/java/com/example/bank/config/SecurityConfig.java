package com.example.bank.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.bank.config.jwt.JwtAuthenticationFilter;
import com.example.bank.domain.user.UserEnum;
import com.example.bank.utils.CustomResponseUtil;

@Configuration
public class SecurityConfig {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Bean // IoC 컨테이너에 BCryptPasswordEncoder() 객체가 등록됨.
    public BCryptPasswordEncoder passwordEncoder() {
        log.debug("디버그 : BCryptPasswordEncoder() 객체가 등록됨.");
        return new BCryptPasswordEncoder();
    }

    // JWT filter 등록이 필요함. (만듦)
    // 모든 필터 등록은 여기서!! (AuthenticationManager 순환 의존 문제로 내부 클래스로 만들어진 듯, 추측임)
    public class CustomSecurityFilterManager extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            builder.addFilter(new JwtAuthenticationFilter(authenticationManager));
            super.configure(builder);
        }

        public HttpSecurity build() {
            return getBuilder();
        }
    }

    // JWT 서버를 만들 예정. Session 사용 안함.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.debug("디버그 : filterChain Bean 등록됨.");
        // iframe 허용 안함. (X-Frame-Options: DENY)
        // Spring Security 6에서는 headers() -> frameOptions() 대신 frameOptions()을 직접 사용합니다.
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.deny()))
                // CSRF 비활성화 (Postman 등에서 테스트 시 필요)
                // 실 서비스에서는 CSRF 공격 방어를 위해 활성화하는 것이 좋습니다.
                .csrf(csrf -> csrf.disable())
                // CORS 비활성화 (만약 @CrossOrigin을 사용하거나 특정 CORS 정책을 설정해야 한다면 활성화)
                .cors(cors -> cors.configurationSource(configurationSource())) // .cors().disable()과 동일
                /*
                 * SessionCreationPolicy.STATELESS
                 * 클라이언트가 로그인 request
                 * 서버는 User 세션 저장
                 * 서버가 response
                 * 세션값 사라짐. (즉 유지하지 않음)
                 * JSESSIONID를 서버쪽에서 관리하지 않겠다는 의미.
                 */
                .sessionManagement(
                        sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 폼 로그인 비활성화
                .formLogin(formLogin -> formLogin.disable())
                // HTTP Basic 인증 비활성화 (브라우저 팝업 창을 이용한 인증)
                .httpBasic(httpBasic -> httpBasic.disable())
                // 필터 적용
                .with(new CustomSecurityFilterManager(), customizer -> customizer.build())
                // ExcpetionTranslationFilter (인증 확인 필터)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) -> { // Exception 가로채기
                            CustomResponseUtil.unAuthentication(response, "로그인을 진행해 주세요.");
                        }))
                                // 요청 권한 설정
                .authorizeHttpRequests(authorize -> authorize
                        // "/api/s/**" 경로는 인증된 사용자만 접근 가능
                        .requestMatchers("/api/s/**").authenticated()
                        // "/api/admin/**" 경로는 "ADMIN" 역할을 가진 사용자만 접근 가능 (ROLE_ 접두사는 자동으로 붙음)
                        .requestMatchers("/api/admin/**").hasRole(""+UserEnum.ADMIN)
                        // 나머지 모든 요청은 허용
                        .anyRequest().permitAll());

        return http.build();
    }

    public CorsConfigurationSource configurationSource() {
        log.debug("디버그 : configurationSource cors 설정이 SecurityFilterChain 등록됨.");
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*"); // GET, POST, PUT, DELETE, PATCH, OPTIONS
        configuration.addAllowedOriginPattern("*"); // 모든 IP 주소 허용 (frontend ip만 허용, react)
        configuration.setAllowCredentials(true); // 클라이언트에서 쿠키 요청 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 모든 주소 요청에 configuration 설정을 넣는다.
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}