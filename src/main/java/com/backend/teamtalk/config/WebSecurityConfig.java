package com.backend.teamtalk.config;

import com.backend.teamtalk.jwt.JwtAccessDeniedHandler;
import com.backend.teamtalk.jwt.JwtAuthenticationEntryPoint;
import com.backend.teamtalk.jwt.JwtSecurityConfig;
import com.backend.teamtalk.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /*
         * H2 Console 은 iframe 을 사용하기 때문에 Header 의 X-Frame-Options 를 비활성화 해야 함 (.disable())
         * 하지만 무작정 disable 처리 하는 것보다 X-Frame-Options 동일 출처일 경우만 허용하도록 설정.
         * h2-console 을 사용해야 하니  permitAll 설정을 해주고 csrf Token 은 비활성화 설정.
         */
        http.headers().frameOptions().sameOrigin();
        http.authorizeRequests() .antMatchers("/h2/**").permitAll();
        http.csrf().disable();

        http
                //exception handling 할 때는 내가 만든 클래스를 쓸 거니까 2개 설정.
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                //jwt 인증이므로 세션은 사용하지 않음.
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)


                .and()
                /*
                 * 요청에 대한 접근 권한 설정
                 * 로그인, 회원가입은 누구에게나 허용해줘야 하니 permitAll 설정
                 * 실수: api 의 method 설정을 안해줘서 오류 찾는 데 시간을 많이 소요 함.
                 */
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/login").permitAll()
                .antMatchers(HttpMethod.POST, "/api/signup").permitAll()

                //테스트 할 때 편하게 확인 하기 위해 설정해놓은 것.
                .antMatchers("/api/users").permitAll()
//                .antMatchers("/h2-console/**").permitAll()
                .antMatchers(HttpMethod.DELETE,"/api/users/{user_id}").permitAll()
//                .antMatchers("/api").permitAll()


                //로그인 한 회원의 board, pin, card, comment 읽기 [get 요청]
                .antMatchers("/main/{username}").permitAll()
                .antMatchers("/api/boards/{board_id}").permitAll()
                .antMatchers("/api/pins/{pin_id}").permitAll()
                .antMatchers("/api/pins").permitAll()
                .antMatchers("/api/cards").permitAll()
                .antMatchers("/api/cards/{card_id}").permitAll()
                .antMatchers("/api/cards/{card_id}/comments").permitAll()

                //board 생성, 수정, 삭제 : 회원만
                .antMatchers(HttpMethod.POST, "/api/boards").hasRole("USER")
                .antMatchers(HttpMethod.PUT, "/api/boards/**").hasRole("USER")
                .antMatchers(HttpMethod.DELETE, "/api/boards/**").hasRole("USER")

                //pin 생성, 수정, 삭제 : 회원만
                .antMatchers(HttpMethod.POST, "/api/pins/{board_id}").hasRole("USER")
                .antMatchers(HttpMethod.PUT, "/api/pins/{pin_id}").hasRole("USER")
                .antMatchers(HttpMethod.DELETE, "/api/pins/{pin_id}").hasRole("USER")

                //card 생성, 수정, 삭제: 회원만
                .antMatchers(HttpMethod.POST, "/api/cards/{pin_id}").hasRole("USER")
                .antMatchers(HttpMethod.PUT, "/api/cards/{card_id}").hasRole("USER")
                .antMatchers(HttpMethod.DELETE, "/api/cards/{card_id}").hasRole("USER")

                //comment 생성, 수정, 삭제: 회원만
                .antMatchers(HttpMethod.POST, "/api/cards/{card_id}/comments").hasRole("USER")
                .antMatchers(HttpMethod.PUT, "/api/cards/{card_id}/comments/{comment_id}").hasRole("USER")
                .antMatchers(HttpMethod.DELETE, "/api/cards/{card_id}/comments/{comment_id}").hasRole("USER")


                .anyRequest().hasRole("ADMIN")

                .and()
                /*
                 * UsernamePasswordAuthenticationFilter 가 작동 되기 전에
                 * 내가 custom 한 JwtAuthenticationFilter 가 먼저 작동 되도록 addFilterBefore 에 설정했던
                 * JwtSecurityConfig 클래스 적용
                 */
                .apply(new JwtSecurityConfig(jwtTokenProvider));
    }

}
