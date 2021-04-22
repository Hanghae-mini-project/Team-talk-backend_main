package com.backend.teamtalk.controller;

import com.backend.teamtalk.dto.LoginDto;
import com.backend.teamtalk.dto.TokenDto;
import com.backend.teamtalk.jwt.JwtAuthenticationFilter;
import com.backend.teamtalk.jwt.JwtTokenProvider;
import com.backend.teamtalk.domain.User;
import com.backend.teamtalk.dto.SignupRequestDto;
import com.backend.teamtalk.repository.UserRepository;
import com.backend.teamtalk.service.UserService;
import jdk.nashorn.internal.parser.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;


    /**
     * username check: 중복체크 버튼 클릭시 / 회원가입 버튼 클릭시 둘 다 적용하는 것이 좋다.
     *
     * @param requestDto
     * @return
     */

    //create user
    @PostMapping("/api/signup")
    public ResponseEntity<User> createUser(@Valid @RequestBody SignupRequestDto requestDto) {
        return ResponseEntity.ok(userService.createUser(requestDto));
    }


    // login
    @PostMapping("/api/login")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginDto loginDto) {    //username, password
        //둘 다 안 뜨고 서버 오류 500 에러 터짐. 맞게 치면 토큰 발행해 줌
        //username 대조
        User member = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("There is nobody by that name."));

        //password 대조
        if (!passwordEncoder.matches((loginDto.getPassword()), member.getPassword())) {
            //로그인 시 입력한 비밀번호와 db에 저장된 비밀번호가 일치하는지?
            throw new IllegalArgumentException("The password is incorrect.");
        }

        //순서 다시 확인할 것
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        //왜 2번이나 하지?
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.createToken(authentication);

        //token 을 header 에도 넣고
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtAuthenticationFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        //response body 에도 던지기
        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);

    }


    //회원가입 후 모든 유저 부르기 임시 테스트용으로 만들어 봄
    //의미 없는 메서드라서 최종본에서는 삭제할 것
    @GetMapping("/api/users")
    public List<User> allUsers() {
        List<User> allUsers = userRepository.findAll();
        return allUsers;
    }

    //delete user
    @DeleteMapping("api/users/{user_id}")
    public String deleteUser(@PathVariable Long user_id) {
        //user 찾아와야 하는데 귀찮으니까 일단 삭제
        userRepository.deleteById(user_id);
        return "delete user: success.";
    }


    //권한 검증 테스트용 (확장 시, admin 사용)
    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<User> getMyUserInfo() {
        //username 반환하게
        return ResponseEntity.ok(userService.getMyUserWithAuthorities().get());
    }
    //권한 검증 테스트용 (관리자만 조회 가능)
    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<User> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserWithAuthorities(username).get());
    }

}
