package com.backend.teamtalk.service;

import com.backend.teamtalk.domain.Authority;
import com.backend.teamtalk.domain.User;
import com.backend.teamtalk.dto.SignupRequestDto;
import com.backend.teamtalk.repository.UserRepository;
import com.backend.teamtalk.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.Optional;


@Service
public class UserService {

    /*
     * 실수: PasswordEncoder configuration 에 bean 으로 등록 해야 함.
     */
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /*
     * 의문: createUser 메서드에 왜 Transactional 어노테이션을 달아야 하지?
     */

    //create user
    @Transactional
    public User createUser(SignupRequestDto requestDto) {
        //username duplication check
        if (userRepository.findOneWithAuthoritiesByUsername(requestDto.getUsername()).orElse(null) != null) {
            throw new RuntimeException("status 409, username is existed");
        }

        //builder 하기
        Authority authority = Authority.builder()
                //기본으로 전부 user 라고 세팅
                .authorityName("ROLE_USER")
                .build();

        User user = User.builder()
                .username(requestDto.getUsername())
                //비밀번호는 그냥 넣으면 안됨
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .skill(requestDto.getSkill())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        return userRepository.save(user);
    }

    //권한 검증 하기
    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(String username) {
        return userRepository.findOneWithAuthoritiesByUsername(username);
    }


    /*
     * 처음 보는 것: .flatMap(userRepository::findOneWithAuthoritiesByUsername) 문법
     *
     * SecurityUtil 의 getCurrentUsername 메서드를 통해 인증 성공한 user 의 username 과
     * userRepository 에서 findOneWithAuthoritiesByUsername 통해 얻은 user 객체
     */
    @Transactional(readOnly = true)
    public Optional<User> getMyUserWithAuthorities() {
        //username 반환
        return SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);
    }



}
