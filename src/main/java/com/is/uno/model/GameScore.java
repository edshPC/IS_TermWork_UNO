package com.is.uno.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "uno_game_score")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class GameScore implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Column(name = "score", nullable = false)
    @Min(value = 0, message = "Счет может быть 0 или больше")
    private Long score;
}
