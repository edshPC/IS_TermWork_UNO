package com.is.uno.controller;

import com.is.uno.dto.api.LoginUserDTO;
import com.is.uno.dto.api.RegisterUserDTO;
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

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterUserDTO registerUserDto) {
        return authService.register(registerUserDto).asResponseEntity();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginUserDTO loginUserDto) {
        return authService.login(loginUserDto).asResponseEntity();
    }
}
