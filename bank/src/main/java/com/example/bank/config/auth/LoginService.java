package com.example.bank.config.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.bank.domain.user.User;
import com.example.bank.domain.user.UserRepository;

@Service
public class LoginService implements UserDetailsService{

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserRepository userRepository;

    // security로 로그인이 될 때, security가 loadUserByUsername를 실행해서 username을 체크!
    // 없으면 오류.
    // 있으면 정상적으로 시큐리티 컨텍스트 내부 세션에 로그인 된 세션이 만들어진다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        log.debug("디버그 : loadUserByUsername 시작");
        User userPS = userRepository.findByUsername(username).orElseThrow(
            ()-> new InternalAuthenticationServiceException("인증 실패")
        );
        // request - 세션 생성 - 세션 사용 - response - 세션 삭제
        return new LoginUser(userPS);
    }
    
}
