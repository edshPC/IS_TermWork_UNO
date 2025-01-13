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

    public Player findByRoomAndUserOrCreate(GameRoom gameRoom, User user) {
        return playerRepository.findByUserAndCurrentRoom(user, gameRoom)
                .orElseGet(() -> Player.builder()
                        .user(user)
                        .currentRoom(gameRoom)
                        .inGameName(user.getUsername())
                        .build());
    }

    public void updatePlayerInGameName(Long id, String newInGameName) {
        Player player = findById(id);
        player.setInGameName(newInGameName);
        playerRepository.save(player);
    }

    public List<PlayerDTO> getPlayerByUsername(String username) {
        List<Player> players = findByUsername(username);
        return players.stream()
                .map(this::toPlayerDTO)
                .collect(Collectors.toList());
    }

    public long countPlayersInRoom(GameRoom gameRoom) {
        return playerRepository.countByCurrentRoom(gameRoom);
    }


    private PlayerDTO toPlayerDTO(Player player) {
        return PlayerDTO.builder()
                .inGameName(player.getInGameName())
                .username(player.getUser().getUsername())
                .roomId(player.getCurrentRoom().getId())
                .build();
    }
}
