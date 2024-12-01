package com.is.uno.dto.api;

import lombok.*;

import java.time.Duration;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class StatisticsDTO {
    private String username;
    private Integer rating;
    private Integer playCount;
    private Integer winCount;
    private Duration timePlayed;
}
