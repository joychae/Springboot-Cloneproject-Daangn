package com.clone.daangnclone.controller;

import com.clone.daangnclone.dto.LoginDto;
import com.clone.daangnclone.dto.TokenDto;
import com.clone.daangnclone.jwt.JwtFilter;
import com.clone.daangnclone.jwt.TokenProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class AuthController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    // 로그인 정보를 받아오는 메소드이다.
    // LoginDto 를 통해 로그인 하고자 하는 유저의 username 과 password 정보를 받아온다.
    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {

        // 중요! 받아온 username 과 password 값으로 인증 전의 Authentication 객체를 생성
        // Authentication 객체를 구현한 UsernamePasswordAuthenticaitonToken 객체를 구현하기 위해서 UserDetails 객체를 이용
        // 그 과정에서 UserDetailsService 에 접근하고, User Table 에 아이디가 없는 유저라면 loadUserByUsername 의 에러 메시지가 반환된다.
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 해당 값을 SecurityContext 에 저장한다.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }
}