package com.is.uno.dto.api;

import com.is.uno.model.Color;
import com.is.uno.model.Type;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CardDTO implements Cloneable {
    private Long id;
    private Type type;
    private Color color;
    private Color newColor;
    private Integer value;

    public boolean canPlaceOn(CardDTO other) {
        if (color == Color.BLACK) return true;
        if (color == other.color ||
            color == other.newColor) return true;
        if (type == Type.NUMBER &&
            other.type == Type.NUMBER &&
            value.equals(other.value)) return true;
        if (type != Type.NUMBER &&
            type == other.type) return true;
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
