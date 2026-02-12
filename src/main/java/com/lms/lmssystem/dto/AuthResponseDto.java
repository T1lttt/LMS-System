package com.lms.lmssystem.dto;

import lombok.Data;

@Data
public class AuthResponseDto {
    private String accessToken;
    private String refreshToken;

    public AuthResponseDto(String accessToken, String refreshToken) {
    }
}
