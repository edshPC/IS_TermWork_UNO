package com.is.uno.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public class GameDTO {
    private Integer maxScore;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long winnerId;
    private Long roomId;
}
