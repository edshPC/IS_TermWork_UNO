package com.is.uno.dto.api;

import com.is.uno.model.Color;
import com.is.uno.model.Type;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CardDTO {
    private Long id;
    private Type type_of_card;
    private Color color_of_card;
    private Integer value;
}
