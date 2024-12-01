package com.is.uno.dto.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public class GameRoomDTO {
    private String roomName;
    private String password;
    private Integer maxPlayers;
    private String owner;
}
