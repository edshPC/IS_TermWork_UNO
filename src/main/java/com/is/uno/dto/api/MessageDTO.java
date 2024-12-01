package com.is.uno.dto.api;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MessageDTO {
    private String senderName;
    private String text;
    private LocalDateTime time;
}
