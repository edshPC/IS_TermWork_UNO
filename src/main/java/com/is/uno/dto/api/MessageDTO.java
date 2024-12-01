package com.is.uno.dto.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public class MessageDTO {
    private String senderName;
    private String text;
    private LocalDateTime time;
}
