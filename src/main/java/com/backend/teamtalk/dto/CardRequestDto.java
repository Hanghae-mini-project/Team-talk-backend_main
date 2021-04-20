package com.backend.teamtalk.dto;

import lombok.Getter;

/**
 * 사실 1.카드 만들기
 * 2. 카드 수정하기
 * 3. 카드 내용 만들기
 * 4. 카드 내용 수정하기
 * 이렇게 만들어야 함.
 */
@Getter
public class CardRequestDto {

    private String title;
    private String description;
//
//    private String comment; //-> 따로 뺄 예정
}
