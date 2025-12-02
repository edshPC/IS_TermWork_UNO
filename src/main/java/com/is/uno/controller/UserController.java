package com.is.uno.controller;

import com.is.uno.dto.DataResponse;
import com.is.uno.dto.SimpleResponse;
import com.is.uno.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<?> getUserProfile(@PathVariable String username) {
        var profile = userService.getUserProfile(username);
        return DataResponse.success(profile);
    }

    @PutMapping("/{username}/inGameName")
    public ResponseEntity<?> updatePlayerInGameName(@PathVariable String username, @RequestBody String newInGameName) {
        userService.updatePlayerInGameName(username, newInGameName);
        return SimpleResponse.success();
    }

}
