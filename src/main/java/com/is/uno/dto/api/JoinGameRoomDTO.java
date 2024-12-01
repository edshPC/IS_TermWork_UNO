package com.is.uno.dto.api;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class JoinGameRoomDTO {
    private String password;
    private String inGameName;
    private Long roomId;
}
