package com.is.uno.dto.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CreateGameRoomDTO {
    private String roomName;
    private String password;
    private Integer maxPlayers;
    private Integer maxScore;
    private String owner;
}
