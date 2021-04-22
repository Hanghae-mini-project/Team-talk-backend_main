package com.backend.teamtalk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


/*
 * @Getter: 주석 처리 해놨더니 로그인 시 http status 406 에러 발생
 * @NoArgsConstructor: 기본 생성자를 만들지 않았더니 로그인 시 http status 406 에러 발생
 * @AllArgsConstructor: token 만들어서 response body 에 던질 때 필요. ( new TokenDto(jwt) )
 */

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {

    private String token;
}
