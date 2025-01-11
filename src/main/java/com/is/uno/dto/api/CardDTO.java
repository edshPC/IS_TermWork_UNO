package com.is.uno.dto.api;

import com.is.uno.model.Color;
import com.is.uno.model.Type;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CardDTO implements Cloneable {
    private Long id;
    private Type type_of_card;
    private Color color_of_card;
    private Integer value;

    public boolean canPlaceOn(CardDTO other) {
        if (color_of_card == Color.BLACK) return true;
        if (color_of_card == other.color_of_card) return true;
        if (type_of_card == Type.NUMBER &&
            other.type_of_card == Type.NUMBER &&
            value.equals(other.value)) return true;
        if (type_of_card != Type.NUMBER &&
            type_of_card == other.type_of_card) return true;
        return false;
    }

    @Override
    public CardDTO clone() {
        try {
            CardDTO clone = (CardDTO) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
