package com.backend.teamtalk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter             //마찬가지로 주석 처리 해놨더니 로그인 시 http status 406에러 발생.
@Builder
@AllArgsConstructor //token 만들어서 response body 에 던질 때 필요하다. new TokenDto(jwt)
@NoArgsConstructor  //기본 생성자를 안만들어놨더니, 로그인 시 http status 406에러 발생.
public class TokenDto {

    private String token;
}
