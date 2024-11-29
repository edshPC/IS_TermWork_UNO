package com.is.uno.controller;

import com.is.uno.dto.AchievementDTO;
import com.is.uno.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5175")
@RequestMapping("/achievement")
public class AchievementController {
    private final AchievementService achievementService;

    @GetMapping("/{username}")
    public List<AchievementDTO> getPlayerAchievements(@PathVariable String username) {
        return achievementService.getPlayerAchievements(username);
    }
}
