package com.is.uno.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.io.Serializable;
import java.time.Duration;

@Entity
@Table(name = "statistics")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class Statistics implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    @Column(name = "rating", nullable = false)
    @Min(value = 0, message = "Рейтинг может быть 0 или больше")
    private Integer rating;

    @Column(name = "playCount", nullable = false)
    @Min(value = 0, message = "Количество игр может быть 0 или больше")
    private Integer playCount;

    @Column(name = "winCount", nullable = false)
    @Min(value = 0, message = "Количество выигранных игр может быть 0 или больше")
    private Integer winCount;

    @Column(name = "time_played", nullable = false)
    private Duration timePlayed;
}
