package com.is.uno.service;

import com.is.uno.dao.GameScoreRepository;
import com.is.uno.dao.UserRepository;
import com.is.uno.dto.api.PlayerDTO;
import com.is.uno.model.GameRoom;
import com.is.uno.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final GameScoreRepository gameScoreRepository;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException(String.format("Username %s not found", username)));
    }

    public User getUserProfile(String username) {
        return findByUsername(username);
    }

    public void updatePlayerInGameName(String username, String newInGameName) {
        User player = findByUsername(username);
        player.setInGameName(newInGameName);
        userRepository.save(player);
    }

   /* public List<PlayerDTO> getPlayerByUsername(String username) {
        List<User> players = findByUsername(username);
        return players.stream()
                .map(this::toPlayerDTO)
                .collect(Collectors.toList());
    }*/

    public long countPlayersInRoom(GameRoom gameRoom) {
        return userRepository.countByCurrentRoom(gameRoom);
    }

    public long calculateTotalScore(User player) {
        Long score = gameScoreRepository.calculatePlayerScore(player);
        if (score == null) return 0;
        return score;
    }

    private PlayerDTO toPlayerDTO(User player) {
        return PlayerDTO.builder()
                .inGameName(player.getInGameName())
                .username(player.getUsername())
                .roomId(player.getCurrentRoom().getId())
                .build();
    }
}
