package com.is.uno.controller;

import com.is.uno.core.UserEvent;
import com.is.uno.dto.DataResponse;
import com.is.uno.model.User;
import com.is.uno.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/statistics")
public class StatisticsController {
    private final StatisticsService statisticsService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @GetMapping
    public ResponseEntity<?> getGlobalStatistics(@AuthenticationPrincipal User user) {
        applicationEventPublisher.publishEvent(new UserEvent(user.getUsername(), UserEvent.Type.VIEW_STATISTICS));
        var stats = statisticsService.getGlobalStatistics();
        return DataResponse.success(stats);
    }
}
