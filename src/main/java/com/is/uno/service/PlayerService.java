package com.is.uno.service;

import com.is.uno.dao.PlayerRepository;
import com.is.uno.dto.PlayerDTO;
import com.is.uno.exception.PlayerNotFoundException;
import com.is.uno.model.GameRoom;
import com.is.uno.model.Player;
import com.is.uno.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final GameRoomService gameRoomService;
    private final UserService userService;

    public Player findById(Long id) {
        return playerRepository.findById(id).orElseThrow(() ->
                new PlayerNotFoundException(String.format("Player %s not found", id)));
    }

    public Player findByUsername(String username) {
        return playerRepository.findByUserUsername(username).orElseThrow(() ->
                new PlayerNotFoundException(String.format("Player %s not found", username)));
    }

    public PlayerDTO createPlayer(PlayerDTO playerDTO) {
        User user = userService.findByUsername(playerDTO.getUsername());
        GameRoom gameRoom = gameRoomService.findById(playerDTO.getRoomId());

        Player player = Player.builder()
                .inGameName(playerDTO.getInGameName())
                .user(user)
                .currentRoom(gameRoom)
                .build();

        player = playerRepository.save(player);
        return toPlayerDTO(player);
    }

    public void updatePlayerInGameName(String username, String newInGameName) {
        Player player = findByUsername(username);
        player.setInGameName(newInGameName);
        player = playerRepository.save(player);
    }

    public PlayerDTO getPlayerByUsername(String username) {
        Player player = findByUsername(username);
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
