package com.backend.teamtalk.jwt;

import lombok.NoArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
 * 내가 만든 JwtTokenProvider 와 JwtAuthenticationFilter 를 WebSecurityConfig 에 등록해야 함.
 * 이를 위해 JwtSecurityConfig 를 만듦.
 *
 * WebSecurityConfig 에 바로 등록해도 되지만, Jwt 만 따로 있는 패키지에서 개별 설정 해줌.
 */

@NoArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private JwtTokenProvider jwtTokenProvider;

    public JwtSecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /*
     * UsernamePasswordAuthenticationFilter 가 작동 되기 전에
     * 내가 custom 한 JwtAuthenticationFilter 가 먼저 작동 되도록
     *
     *  request 요청이 왔을 떄, intercept 해서 내가 만든 filter 가 처리 할 수 있도록
     */

    @Override
    public void configure(HttpSecurity http) {
        JwtAuthenticationFilter customFilter = new JwtAuthenticationFilter(jwtTokenProvider);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
