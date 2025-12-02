package com.is.uno.service;

import com.is.uno.core.UserEvent;
import com.is.uno.dao.AchievementRepository;
import com.is.uno.dao.UserRepository;
import com.is.uno.dto.api.AchievementDTO;
import com.is.uno.model.Achievement;
import com.is.uno.model.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
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

    public void tryAddAchievementToUser(String username, String achievementId) {
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

    @EventListener
    public void onUserEvent(UserEvent event) {
        String achievement = switch (event.getType()) {
            case REGISTER -> "registration";
            case VIEW_STATISTICS -> "view_statistics";
            case CREATE_ROOM -> "first_room_creation";
            case WIN -> switch (event.getValue()) {
                case 1 -> "first_win";
                case 5 -> "five_wins";
                case 10 -> "ten_wins";
                default -> null;
            };
            case PLAY -> switch (event.getValue()) {
                case 1 -> "first_play";
                case 5 -> "five_plays";
                case 10 -> "ten_plays";
                default -> null;
            };
        };

        if (achievement != null) {
            tryAddAchievementToUser(event.getUsername(), achievement);
        }
    }

    private AchievementDTO toAchievementDTO(Achievement achievement) {
        return AchievementDTO.builder()
                .name(achievement.getName())
                .description(achievement.getDescription())
                .build();
    }
}
