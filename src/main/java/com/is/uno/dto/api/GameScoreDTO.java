package com.is.uno.dto.api;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class GameScoreDTO {
    private Long playerId;
    private Long gameId;
    private Integer score;
}
