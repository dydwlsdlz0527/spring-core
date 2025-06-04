package com.example.bank.temp;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

public class RegexTest {

    // 한글만 된다.
    @Test
    public void hangul_test() throws Exception {
        String value = "가";
        boolean result = Pattern.matches("^[가-힣]+$", value);
        System.out.println("테스트 : " + result);
    }

    // 한글은 안 된다.
    @Test
    public void not_hangul_test() throws Exception {
        String value = "abc";
        boolean result = Pattern.matches("^[^ㄱ-ㅎ가-힣]*$", value);
        System.out.println("테스트 : " + result);
    }

    // 영어만 된다.
    @Test
    public void english_test() throws Exception {
        String value = "a";
        boolean result = Pattern.matches("^[a-zA-Z]+$", value);
        System.out.println("테스트 : " + result);
    }

    // 영어는 안 된다.
    @Test
    public void not_english_test() throws Exception {
        String value = "가1";
        boolean result = Pattern.matches("^[^a-zA-Z]*$", value);
        System.out.println("테스트 : " + result);
    }

    // 영어와 숫자만 된다.
    @Test
    public void english_number_test() throws Exception {
        String value = "abc123!";
        boolean result = Pattern.matches("^[a-zA-Z0-9]+$", value);
        System.out.println("테스트 : " + result);
    }

    // 영어만 되고 길이는 최소 2 최대 4이다.
    @Test
    public void english_length_test() throws Exception {
        String value = "abcd";
        boolean result = Pattern.matches("^[a-zA-Z]{2,4}$", value);
        System.out.println("테스트 : " + result);
    }

    // username, email, fullname
    @Test
    public void user_username_test() throws Exception {
        String username = "test";
        boolean result = Pattern.matches("^[a-zA-Z0-9]{2,20}$", username);
        System.out.println("테스트 : " + result);
    }

    @Test
    public void user_fullname_test() throws Exception {
        String fullname = "김용진";
        boolean result = Pattern.matches("^[a-zA-Z가-힣]{1,20}$", fullname);
        System.out.println("테스트 : " + result);
    }

    @Test
    public void user_email_test() throws Exception {
        String fullname = "test@test.com";
        boolean result = Pattern.matches("^[a-zA-Z0-9]{2,10}+@[a-zA-Z0-9]{2,6}\\.[a-zA-Z]{2,3}$", fullname);
        System.out.println("테스트 : " + result);
    }
}
