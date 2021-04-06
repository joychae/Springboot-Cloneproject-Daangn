package com.clone.daangnclone.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    public static final String AUTHORIZATION_HEADER = "Authorization";

    // TokenProvider 를 의존성 주입받는다.
    private TokenProvider tokenProvider;

    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    // GenericFilterBean 클래스를 상속 받아서, doFilter 메소드를 오버라이드 한다.
    // 실제 필터링 로직은 doFilter 메소드에 작성된다.
    // 토큰의 인증정보를 SecurityContext 에 저장하는 역할을 수행한다.
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        // 1.resolveToken 을 통해 토큰을 받아와서
        String jwt = resolveToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();

        // 2.유효성 검증을 하고
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            // 3. 정상 토근이면 getAuthentication 메소드로 토큰값을 이용해 인증된 Authenticaiton 객체를 반환
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            // 4.인증된 Authentication 객체를 SecurityContext 에 저장한다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
        } else {
            logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    // Token 값을 해석해주는 메소드이다.
    // 헤더를 통해 온 token 값을 복호화 한다.
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
