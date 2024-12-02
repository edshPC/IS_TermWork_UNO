package com.is.uno.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "uno_game_room")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class GameRoom implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_name", nullable = false)
    private String roomName;

    @Column(name = "password")
    private String password;

    private Boolean visible;

    @Column(name = "max_players", nullable = false)
    @Min(value = 2, message = "Минимальное количество игроков: 2")
    @Max(value = 8, message = "Максимальное количество игроков: 8")
    private Integer maxPlayers;

    @Column(name = "max_score")
    @Min(value = 0, message = "Максимальный счет может быть 0 или больше")
    private Integer maxScore;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "username", nullable = false)
    private User owner;
}
