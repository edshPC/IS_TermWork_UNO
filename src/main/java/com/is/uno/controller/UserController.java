package com.is.uno.controller;

import com.is.uno.dto.DataResponse;
import com.is.uno.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<?> getUserProfile(@PathVariable String username) {
        var profile = userService.getUserProfile(username);
        return DataResponse.success(profile);
    }
}
