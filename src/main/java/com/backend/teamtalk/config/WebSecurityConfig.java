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

//    @Override
//    public void configure(WebSecurity web) {
//        web.ignoring()  //무시하고 넘겨줘라.
//                .antMatchers(
//                        "/h2-console/**"    //test용
//                        ,"/favicon.ico"
//                        ,"/error"
//                );
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.headers().frameOptions().sameOrigin(); //x-frame-options 동일 출처일경우만
        http.authorizeRequests() .antMatchers("/h2/**").permitAll(); //h2 URI에 대한 권한 허가
        http.csrf().disable(); //CSRF Token 비활성화

        http
                //jwt 토큰 사용하니까 csrf 보안 토큰은 disable
//                .csrf().disable()

                //exception handling 할 때는 내가 만든 걸 쓸 거니까 2 개 추가 해주고
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // h2-console 사용
//                .and()
//                .headers()
//                .frameOptions()
////                .sameOrigin()
//                .disable()


                //jwt 인증이므로 세션은 사용하지 않는다.
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                //요청에 대한 접근 권한 설정
                .authorizeRequests()
                //로그인, 회원가입은 토큰이 없는 상태에서 들어오는 요청이니까 열어줘야 한다
                //method 지정 안해줘서.... 하루 날렸네
                .antMatchers(HttpMethod.POST, "/api/login").permitAll()
                .antMatchers(HttpMethod.POST, "/api/signup").permitAll()

                //test용
                .antMatchers("/api/users").permitAll()  //테스트용. 모든 사람에게 오픈
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers(HttpMethod.DELETE,"/api/users/{user_id}").permitAll()
                .antMatchers("/api").permitAll()

                //test:
                //로그인 한 회원의 보드, 핀, 카드 읽기 (get 요청), 추가: 코멘트
                .antMatchers("/main/{username}").permitAll()
                .antMatchers("/api/boards/{board_id}").permitAll()
                .antMatchers("/api/pins/{pin_id}").permitAll()
                .antMatchers("/api/pins").permitAll()
                .antMatchers("/api/cards").permitAll()
                .antMatchers("/api/cards/{card_id}").permitAll()
                .antMatchers("/cards/{card_id}/comments").permitAll()

                //board 생성, 수정, 삭제 : 회원만 (우선은)
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
                .antMatchers(HttpMethod.POST, "/cards/{card_id}/comments").hasRole("USER")
                .antMatchers(HttpMethod.PUT, "/cards/{card_id}/comments/{comment_id}").hasRole("USER")
                .antMatchers(HttpMethod.DELETE, "/cards/{card_id}/comments/{comment_id}").hasRole("USER")



                .anyRequest().hasRole("ADMIN")   //이렇게 설정하는 게 아닌가? 안먹히는데 -> 맞고

                .and()
                .apply(new JwtSecurityConfig(jwtTokenProvider));
    }

}
