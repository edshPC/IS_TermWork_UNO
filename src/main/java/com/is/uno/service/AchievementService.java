package com.is.uno.service;

import com.is.uno.dao.AchievementRepository;
import com.is.uno.dao.UserRepository;
import com.is.uno.dto.AchievementDTO;
import com.is.uno.model.Achievement;
import com.is.uno.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AchievementService {
    private final AchievementRepository achievementRepository;
    private final UserRepository userRepository;

    public List<AchievementDTO> getPlayerAchievements(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("Username %s not found", username)
                ));
        return user.getAchievements().stream()
                .map(this::toAchievementDTO)
                .collect(Collectors.toList());
    }

    public void addAchievementToUser(String username, Achievement achievement) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("Username %s not found", username)
                ));
        user.getAchievements().add(achievement);
        userRepository.save(user);
    }

    private AchievementDTO toAchievementDTO(Achievement achievement) {
        return AchievementDTO.builder()
                .name(achievement.getName())
                .description(achievement.getDescription())
                .build();
    }
}
