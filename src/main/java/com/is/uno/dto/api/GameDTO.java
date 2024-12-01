package com.is.uno.dto.api;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class GameDTO {
    private Integer maxScore;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long winnerId;
    private Long roomId;
}
