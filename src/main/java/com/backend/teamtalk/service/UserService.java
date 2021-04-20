package com.backend.teamtalk.service;

import com.backend.teamtalk.domain.Authority;
import com.backend.teamtalk.domain.User;
//import com.backend.teamtalk.domain.UserAuthority;
import com.backend.teamtalk.dto.SignupRequestDto;
import com.backend.teamtalk.repository.UserRepository;
import com.backend.teamtalk.util.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
//@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;  //error: configuration 에 bean 으로 등록 해야 한다.

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //create user
    @Transactional  //왜?
    public User createUser(SignupRequestDto requestDto) {
        //username duplication check
        if (userRepository.findOneWithAuthoritiesByUsername(requestDto.getUsername()).orElse(null) != null) {
            throw new RuntimeException("status 409, username is existed");
        }
        //password matching check (프론트에서 유효성 검사)
//        if (!requestDto.getPassword().equals(requestDto.getPasswordCheck())) {
//            throw new RuntimeException("status 400, password is not match");
//        }

        //builder 하기
        Authority authority = Authority.builder()
                //기본으로 전부 user 라고 세팅
                .authorityName("ROLE_USER")
                .build();

        User user = User.builder()
                .username(requestDto.getUsername())
                //비밀번호는 그냥 넣으면 안된다.
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .skill(requestDto.getSkill())
                .authorities(Collections.singleton(authority))
//                .userAuthorities((Set<UserAuthority>) authority)
                .activated(true)
                .build();

        return userRepository.save(user);
    }

    //권한 검증 하기
    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(String username) {
        return userRepository.findOneWithAuthoritiesByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<User> getMyUserWithAuthorities() {
        //username 반환
        return SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);
    }



}
