package com.is.uno.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "uno_card")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class Card implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_of_card", nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type_of_card;

    @Column(name = "color_of_card", nullable = false)
    @Enumerated(EnumType.STRING)
    private Color color_of_card;

    @Min(value = 0, message = "Цифра карты может быть 0 или больше")
    @Max(value = 9, message = "Цифра карты может быть 9 или меньше")
    private Integer value;
}
