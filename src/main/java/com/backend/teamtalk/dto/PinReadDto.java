package com.backend.teamtalk.dto;

import com.backend.teamtalk.domain.Pin;
import lombok.Getter;

@Getter
public class PinReadDto {

    private Long boardId;

    private Pin pin;
}
