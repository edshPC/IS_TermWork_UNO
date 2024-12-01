package com.is.uno.dto.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public class GameScoreDTO {
    private Long playerId;
    private Long gameId;
    private Integer score;
}
