package com.backend.teamtalk.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

/**
 * 검증이 끝난 jwt 로부터 유저 정보를 받아와서 UsernamePasswordAuthenticationFilter 로 전달해야 함.
 *
 * SecurityContext 에 Authentication 객체가 저장되는 시점:
 *      JwtAuthenticationFilter 의 doFilter 메소드에서 Request 가 들어올 때 SecurityContext 에 Authentication 객체를 저장.
 */

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    // 주의: 왜 fjnal 을 안붙이는지 생각할 것. 습관처럼 final 붙이지 말 것.
    private JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    //토큰 인증정보를 SecurityContext 에 저장하자.
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        //header 에서 token 받아오기
        String token = resolveToken((HttpServletRequest) request);
        //요청 URI 받기
        String requestURI = httpServletRequest.getRequestURI();

        //이 토큰이 유효하다면?
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            //인증 정보 받아와서
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            //SecurityContext 에 넣자
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("SecurityContext 에 '{}' 인증 정보 저장, uri: {}", authentication.getName(), requestURI);
        } else {
            log.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }

        chain.doFilter(request, response);
    }

    //header 에서 토큰 정보 꺼내기
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            //뒤에 있는 토큰 값만 리턴
            return bearerToken.substring(7);
        }
        return null;
    }
}
