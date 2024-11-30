package com.is.uno.controller;

import com.is.uno.dto.AchievementDTO;
import com.is.uno.model.User;
import com.is.uno.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5175")
@RequestMapping("/api/achievement")
public class AchievementController {
    private final AchievementService achievementService;

    @GetMapping("/get-all")
    public List<AchievementDTO> getPlayerAchievements(@AuthenticationPrincipal User user) {
        return achievementService.getPlayerAchievements(user.getUsername());
    }
}
