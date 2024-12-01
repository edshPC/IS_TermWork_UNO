package com.is.uno.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AuthResponse extends Response<AuthResponse> {
    private String username;
    private LocalDateTime registrationDate;
    private String token;
    private final String tokenType = "Bearer ";
}
