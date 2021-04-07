package com.clone.daangnclone.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    // tokenProvider 의존성 주입 받는다.
    private TokenProvider tokenProvider;

    public JwtSecurityConfig(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    // configure 메소드를 오버라이드 해서 JwtFilter 를 통해 Security 로직에 필터를 등록한다.
    // UsernamePasswordAuthenticationFilter 가 실행되기 전에 커스텀한 JwtFilter 가 실행되도록 지정한다.
    // 이를 SecurityConfig 흐름에도 적용시켜 주어야 한다.
    @Override
    public void configure(HttpSecurity http) {
        JwtFilter customFilter = new JwtFilter(tokenProvider);
        // username, password 를 검사하는 필터를 내가 만튼 커스텀 필터로 하겠다는 지시
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}