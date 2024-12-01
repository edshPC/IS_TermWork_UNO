package com.is.uno.controller;

import com.is.uno.dto.DataResponse;
import com.is.uno.dto.StatisticsDTO;
import com.is.uno.model.User;
import com.is.uno.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/statistics")
public class StatisticsController {
    private final StatisticsService statisticsService;

    @GetMapping()
    public ResponseEntity<?> getGlobalStatistics() {
        var stats = statisticsService.getGlobalStatistics();
        return DataResponse.success(stats);
    }
}
