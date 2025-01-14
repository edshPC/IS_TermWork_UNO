package com.is.uno.service;

import com.is.uno.dao.StatisticsRepository;
import com.is.uno.dto.api.StatisticsDTO;
import com.is.uno.model.*;
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
    private final AchievementService achievementService;

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
        Player player = gameScore.getPlayer();
        User user = player.getUser();
        Statistics statistics = findByUser(user);
        Game game = gameScore.getGame();

        statistics.setPlayCount(statistics.getPlayCount() + 1);
        if (statistics.getPlayCount() == 1) {
            achievementService.addFirstPlayAchievement(user.getUsername());
        }
        if (statistics.getPlayCount() == 5) {
            achievementService.addFivePlayAchievement(user.getUsername());
        }
        if (statistics.getPlayCount() == 10) {
            achievementService.addTenPlayAchievement(user.getUsername());
        }
        if (game.getWinner().equals(player)) {
            statistics.setWinCount(statistics.getWinCount() + 1);
            if (statistics.getWinCount() == 1) {
                achievementService.addFirstWinAchievement(user.getUsername());
            }
            if (statistics.getWinCount() == 5) {
                achievementService.addFiveWinAchievement(user.getUsername());
            }
            if (statistics.getWinCount() == 10) {
                achievementService.addTenWinAchievement(user.getUsername());
            }
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
