package com.is.uno.config;

import com.is.uno.model.Color;
import com.is.uno.model.Type;
import lombok.Data;

@Data
public class Card {
    private Type type;
    private Color color;
    private Integer value;
    private Double weight;
}
