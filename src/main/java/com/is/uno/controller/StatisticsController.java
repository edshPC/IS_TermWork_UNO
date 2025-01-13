package com.is.uno.controller;

import com.is.uno.dto.DataResponse;
import com.is.uno.model.User;
import com.is.uno.service.AchievementService;
import com.is.uno.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/statistics")
public class StatisticsController {
    private final StatisticsService statisticsService;
    private final AchievementService achievementService;

    @GetMapping()
    public ResponseEntity<?> getGlobalStatistics(@AuthenticationPrincipal User user) {
        achievementService.addViewStatisticsAchievement(user.getUsername());
        var stats = statisticsService.getGlobalStatistics();
        return DataResponse.success(stats);
    }
}
