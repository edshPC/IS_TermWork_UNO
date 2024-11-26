package com.is.uno.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AuthResponseDTO {
    private String username;
    private LocalDateTime registrationDate;
    private String token;
    private final String tokenType = "Bearer ";

    public AuthResponseDTO(String username, LocalDateTime registrationDate, String token) {
        this.username = username;
        this.registrationDate = registrationDate;
        this.token = token;
    }
}