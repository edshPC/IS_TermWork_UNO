package com.is.uno.controller;

import com.is.uno.model.User;
import com.is.uno.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5175")
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/{username}")
    public User getUserProfile(@PathVariable String username) {
        return userService.getUserProfile(username);
    }
}
