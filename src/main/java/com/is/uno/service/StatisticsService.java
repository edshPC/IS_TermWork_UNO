package com.is.uno.service;

import com.is.uno.dao.StatisticsRepository;
import com.is.uno.dao.UserRepository;
import com.is.uno.dto.StatisticsDTO;
import com.is.uno.model.GameScore;
import com.is.uno.model.Statistics;
import com.is.uno.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final StatisticsRepository statisticsRepository;
    private final UserRepository userRepository;

    public List<StatisticsDTO> getGlobalStatistics() {
        List<Statistics> statisticsList = statisticsRepository.findAll();
        return statisticsList.stream()
                .map(this::toStatisticsDTO)
                .collect(Collectors.toList());
    }

    public void updatePlayerStatistics(String username, GameScore gameScore) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("Username %s not found", username)
                ));
        Statistics statistics = statisticsRepository.findByUsername(username)
                .orElseGet(() -> initializeStatistics(user));

        statistics.setPlayCount(statistics.getPlayCount() + 1);
        statistics.setWinCount(statistics.getWinCount() + (gameScore.getScore() > 0 ? 1 : 0));
        Duration gameDuration = Duration.between(gameScore.getGameId().getStartTime(), gameScore.getGameId().getEndTime());
        statistics.setTimePlayed(statistics.getTimePlayed().plus(gameDuration));
        statisticsRepository.save(statistics);
    }

    private Statistics initializeStatistics(User user) {
        Statistics newStatistics = Statistics.builder()
                .rating(0)
                .playCount(0)
                .winCount(0)
                .timePlayed(Duration.ZERO)
                .userId(user)
                .build();
        return statisticsRepository.save(newStatistics);
    }

    private StatisticsDTO toStatisticsDTO(Statistics statistics) {
        return StatisticsDTO
                .builder()
                .username(statistics.getUserId().getUsername())
                .rating(statistics.getRating())
                .playCount(statistics.getPlayCount())
                .winCount(statistics.getWinCount())
                .timePlayed(statistics.getTimePlayed())
                .build();
    }
}
