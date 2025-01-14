package com.is.uno.controller;

import com.is.uno.dto.api.LoginUserDTO;
import com.is.uno.dto.api.RegisterUserDTO;
import com.is.uno.service.AchievementService;
import com.is.uno.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final AchievementService achievementService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterUserDTO registerUserDto) {
        ResponseEntity<?> response = authService.register(registerUserDto).asResponseEntity();
        if (response.getStatusCode().is2xxSuccessful()) {
            achievementService.addRegistrationAchievement(registerUserDto.getUsername());
        }
        return response;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginUserDTO loginUserDto) {
        return authService.login(loginUserDto).asResponseEntity();
    }
}
