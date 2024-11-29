package com.is.uno.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public class PlayerDTO {
    private String inGameName;
    private String username;
    private Long roomId;
}
