package com.is.uno.controller;

import com.is.uno.dto.StatisticsDTO;
import com.is.uno.model.User;
import com.is.uno.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5175")
@RequestMapping("/statistics")
public class StatisticsController {
    private final StatisticsService statisticsService;

    @GetMapping()
    public List<StatisticsDTO> getGlobalStatistics() {
        return statisticsService.getGlobalStatistics();
    }
}
