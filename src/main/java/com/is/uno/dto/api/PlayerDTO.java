package com.is.uno.dto.api;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PlayerDTO {
    private String inGameName;
    private String username;
    private Long roomId;
}
