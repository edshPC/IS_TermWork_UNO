package com.is.uno.service;

import com.is.uno.dao.StatisticsRepository;
import com.is.uno.dto.api.StatisticsDTO;
import com.is.uno.model.GameScore;
import com.is.uno.model.Statistics;
import com.is.uno.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final StatisticsRepository statisticsRepository;
    private final UserService userService;

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

    public void updatePlayerStatistics(String username, GameScore gameScore) {
        User user = userService.findByUsername(username);
        Statistics statistics = findByUser(user);

        statistics.setPlayCount(statistics.getPlayCount() + 1);
        statistics.setWinCount(statistics.getWinCount() + (gameScore.getScore() > 0 ? 1 : 0));
        Duration gameDuration = Duration.between(gameScore.getGame().getStartTime(), gameScore.getGame().getEndTime());
        statistics.setTimePlayed(statistics.getTimePlayed().plus(gameDuration));
        statisticsRepository.save(statistics);
    }

    private Statistics initializeStatistics(User user) {
        Statistics newStatistics = Statistics.builder()
                .rating(0)
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
