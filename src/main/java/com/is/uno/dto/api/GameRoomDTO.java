package com.is.uno.dto.api;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class GameRoomDTO {
    private Long id;
    private String roomName;
    private String password;
    private Boolean visible;
    private Integer maxPlayers;
    private Integer maxScore;
    private String owner;
}
