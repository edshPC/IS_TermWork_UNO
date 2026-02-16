package com.is.uno.service;

import com.is.uno.core.UserEvent;
import com.is.uno.dao.StatisticsRepository;
import com.is.uno.dto.api.StatisticsDTO;
import com.is.uno.model.Game;
import com.is.uno.model.GameScore;
import com.is.uno.model.Statistics;
import com.is.uno.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final StatisticsRepository statisticsRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    private Statistics findByUser(User user) {
        return statisticsRepository.findByUserUsername(user.getUsername())
                .orElseGet(() -> initializeStatistics(user));
    }

    public List<StatisticsDTO> getGlobalStatistics() {
        List<Statistics> statisticsList = statisticsRepository.findAll();
        return statisticsList.stream()
                .map(this::toStatisticsDTO)
                .collect(Collectors.toList());
    }

    public void updatePlayerStatistics(GameScore gameScore) {
        User user = gameScore.getUser();
        Statistics statistics = findByUser(user);
        Game game = gameScore.getGame();

        statistics.setPlayCount(statistics.getPlayCount() + 1);
        applicationEventPublisher.publishEvent(
                new UserEvent(user.getUsername(), UserEvent.Type.PLAY, statistics.getPlayCount()));
        if (game.getWinner().equals(user)) {
            statistics.setWinCount(statistics.getWinCount() + 1);
            applicationEventPublisher.publishEvent(
                    new UserEvent(user.getUsername(), UserEvent.Type.WIN, statistics.getWinCount()));
        }
        Duration gameDuration = Duration.between(gameScore.getGame().getStartTime(), gameScore.getGame().getEndTime());
        statistics.setTimePlayed(statistics.getTimePlayed().plus(gameDuration));
        statistics.setRating(statistics.getRating() + gameScore.getRatingGain());

        statisticsRepository.save(statistics);
    }

    private Statistics initializeStatistics(User user) {
        Statistics newStatistics = Statistics.builder()
                .rating(0L)
                .playCount(0)
                .winCount(0)
                .timePlayed(Duration.ZERO)
                .user(user)
                .build();
        return statisticsRepository.save(newStatistics);
    }

    private StatisticsDTO toStatisticsDTO(Statistics statistics) {
        return StatisticsDTO
                .builder()
                .username(statistics.getUser().getUsername())
                .rating(statistics.getRating())
                .playCount(statistics.getPlayCount())
                .winCount(statistics.getWinCount())
                .timePlayed(statistics.getTimePlayed())
                .build();
    }
}
