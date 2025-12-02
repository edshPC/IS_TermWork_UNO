package com.is.uno.controller;

import com.is.uno.dto.DataResponse;
import com.is.uno.model.User;
import com.is.uno.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/achievements")
public class AchievementController {
    private final AchievementService achievementService;

    @GetMapping
    public ResponseEntity<?> getPlayerAchievements(@AuthenticationPrincipal User user) {
        var achievements = achievementService.getPlayerAchievements(user.getUsername());
        return DataResponse.success(achievements);
    }
}
