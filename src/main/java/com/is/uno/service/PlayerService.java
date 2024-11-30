package com.is.uno.service;

import com.is.uno.dao.GameRoomRepository;
import com.is.uno.dao.PlayerRepository;
import com.is.uno.dao.UserRepository;
import com.is.uno.dto.PlayerDTO;
import com.is.uno.exception.GameRoomNotFoundException;
import com.is.uno.exception.PlayerNotFoundException;
import com.is.uno.model.GameRoom;
import com.is.uno.model.Player;
import com.is.uno.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;
    private final GameRoomRepository gameRoomRepository;

    public PlayerDTO createPlayer(PlayerDTO playerDTO) {
        User user = userRepository.findByUsername(playerDTO.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("Username %s not found", playerDTO.getUsername())
                ));
        GameRoom gameRoom = gameRoomRepository.findById(playerDTO.getRoomId())
                .orElseThrow(() -> new GameRoomNotFoundException(
                        String.format("Game room %s not found", playerDTO.getRoomId())
                ));

        Player player = Player.builder()
                .inGameName(playerDTO.getInGameName())
                .user(user)
                .currentRoom(gameRoom)
                .build();

        player = playerRepository.save(player);
        return toPlayerDTO(player);
    }

    public PlayerDTO updatePlayerInGameName(String username, String newInGameName) {
        Player player = playerRepository.findByUserUsername(username)
                .orElseThrow(() -> new PlayerNotFoundException(
                        String.format("Player %s not found", username)
                ));

        player.setInGameName(newInGameName);
        player = playerRepository.save(player);
        return toPlayerDTO(player);
    }

    public PlayerDTO getPlayerByUsername(String username) {
        Player player = playerRepository.findByUserUsername(username)
                .orElseThrow(() -> new PlayerNotFoundException(
                        String.format("Player %s not found", username)
                ));
        return toPlayerDTO(player);
    }

    private PlayerDTO toPlayerDTO(Player player) {
        return PlayerDTO.builder()
                .inGameName(player.getInGameName())
                .username(player.getUser().getUsername())
                .roomId(player.getCurrentRoom().getId())
                .build();
    }
}
