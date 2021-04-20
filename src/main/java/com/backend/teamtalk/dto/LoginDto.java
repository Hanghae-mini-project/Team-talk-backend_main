package com.backend.teamtalk.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    @NotNull
    private String username;

    @NotNull
    private String password;
}
