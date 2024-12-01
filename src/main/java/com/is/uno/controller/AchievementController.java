package com.is.uno.controller;

import com.is.uno.dto.DataResponse;
import com.is.uno.model.User;
import com.is.uno.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/achievement")
public class AchievementController {
    private final AchievementService achievementService;

    @GetMapping("/get-all")
    public ResponseEntity<?> getPlayerAchievements(@AuthenticationPrincipal User user) {
        var achievements = achievementService.getPlayerAchievements(user.getUsername());
        return DataResponse.success(achievements);
    }
}
