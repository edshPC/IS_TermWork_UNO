package com.is.uno.service;

import com.is.uno.dao.PlayerRepository;
import com.is.uno.dto.api.PlayerDTO;
import com.is.uno.exception.PlayerNotFoundException;
import com.is.uno.model.GameRoom;
import com.is.uno.model.Player;
import com.is.uno.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<Player> findByUsername(String username) {
        List<Player> players = playerRepository.findByUserUsername(username);
        if (players.isEmpty()) {
            throw new PlayerNotFoundException(String.format("Player with username %s not found", username));
        }
        return players;
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

    public Player findByInGameNameAndRoomId(String inGameName, Long roomId) {
        return playerRepository.findByInGameNameAndRoomId(inGameName, roomId).orElseThrow(() ->
                new PlayerNotFoundException(String.format("Player %s not found", inGameName)));
    }

    public void updatePlayerInGameName(String inGameName, Long roomId, String newInGameName) {
        Player player = findByInGameNameAndRoomId(inGameName, roomId);
        player.setInGameName(newInGameName);
        playerRepository.save(player);
    }

    public List<PlayerDTO> getPlayerByUsername(String username) {
        List<Player> players = findByUsername(username);
        return players.stream()
                .map(this::toPlayerDTO)
                .collect(Collectors.toList());
    }

    private PlayerDTO toPlayerDTO(Player player) {
        return PlayerDTO.builder()
                .inGameName(player.getInGameName())
                .username(player.getUser().getUsername())
                .roomId(player.getCurrentRoom().getId())
                .build();
    }
}
