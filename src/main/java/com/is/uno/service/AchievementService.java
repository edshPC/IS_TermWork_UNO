package com.is.uno.service;

import com.is.uno.dao.AchievementRepository;
import com.is.uno.dao.UserRepository;
import com.is.uno.dto.api.AchievementDTO;
import com.is.uno.model.Achievement;
import com.is.uno.model.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AchievementService {
    private final AchievementRepository achievementRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public List<AchievementDTO> getPlayerAchievements(String username) {
        User user = userService.findByUsername(username);
        return user.getAchievements().stream()
                .map(this::toAchievementDTO)
                .collect(Collectors.toList());
    }

    public void addAchievementToUser(String username, String achievementId) {
        User user = userService.findByUsername(username);

        boolean alreadyHasAchievement = user.getAchievements()
                .stream()
                .anyMatch(achievement -> achievement.getId().equals(achievementId));
        if (alreadyHasAchievement) {
            return;
        }

        Optional<Achievement> achievement = achievementRepository.findById(achievementId);
        if (achievement.isEmpty()) {
            throw new EntityNotFoundException("Achievement not found: " + achievementId);
        }

        user.getAchievements().add(achievement.get());
        userRepository.save(user);
    }

    public void addViewStatisticsAchievement(String username) {
        addAchievementToUser(username, "view_statistics");
    }

    public void addRegistrationAchievement(String username) {
        addAchievementToUser(username, "registration");
    }

    public void addFirstRoomCreationAchievement(String username) {
        addAchievementToUser(username, "first_room_creation");
    }

    public void addFirstWinAchievement(String username) {
        addAchievementToUser(username, "first_win");
    }

    public void addFiveWinAchievement(String username) {
        addAchievementToUser(username, "five_wins");
    }

    public void addTenWinAchievement(String username) {
        addAchievementToUser(username, "ten_wins");
    }

    public void addFirstPlayAchievement(String username) {
        addAchievementToUser(username, "first_play");
    }

    public void addFivePlayAchievement(String username) {
        addAchievementToUser(username, "five_plays");
    }

    public void addTenPlayAchievement(String username) {
        addAchievementToUser(username, "ten_plays");
    }

    private AchievementDTO toAchievementDTO(Achievement achievement) {
        return AchievementDTO.builder()
                .name(achievement.getName())
                .description(achievement.getDescription())
                .build();
    }
}
