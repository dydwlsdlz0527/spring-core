package com.example.bank.config.jwt;


/**
 * SECRET은 노출되면 안된다. (환경변수, 클라우드 AWS - 환경변수, 파일에 있는 것을 읽을 수도 있고!!)
 * REFRESH TOKEN
 */
public interface JwtVO {

    public static final String SECRET = "secret"; //HS256 (대칭키)
    public static final int EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7; // 일주일
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER = "Authorization";

}
