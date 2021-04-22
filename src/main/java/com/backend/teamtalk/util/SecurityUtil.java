package com.backend.teamtalk.util;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;


@Slf4j
@NoArgsConstructor
public class SecurityUtil {

    /*
     * SecurityContext 에서 Authentication 객체를 꺼내오고, 이 객체를 통해 username 을 리턴.
     * 시도: 다음 로그인 기능 구현 때 getCurrentUsername 메서드를 이용해서 board 만들어 볼 것
     *
     * SecurityContext 에 Authentication 객체가 저장되는 시점:
     *      JwtAuthenticationFilter 의 doFilter 메소드에서 Request 가 들어올 때 SecurityContext 에 Authentication 객체를 저장.
     */

    public static Optional<String> getCurrentUsername() {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.debug("Security Context 에 인증 정보가 없습니다.");
            return Optional.empty();
        }

        String username = null;

        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            username = springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            username = (String) authentication.getPrincipal();
        }

        return Optional.ofNullable(username);
    }
}
