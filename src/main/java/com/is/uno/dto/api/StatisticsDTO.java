package com.is.uno.dto.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public class StatisticsDTO {
    private String username;
    private Integer rating;
    private Integer playCount;
    private Integer winCount;
    private Duration timePlayed;
}
