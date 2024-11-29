package com.is.uno.dto.GameRoom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public class JoinGameRoomDTO {
    private String username;
    private String inGameName;
    private Long roomId;
}
