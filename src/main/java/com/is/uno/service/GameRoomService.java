package com.is.uno.service;

import com.is.uno.dao.GameRoomRepository;
import com.is.uno.dao.PlayerRepository;
import com.is.uno.dto.api.GameRoomDTO;
import com.is.uno.dto.api.JoinGameRoomDTO;
import com.is.uno.exception.ForbiddenException;
import com.is.uno.exception.GameRoomNotFoundException;
import com.is.uno.model.GameRoom;
import com.is.uno.model.Player;
import com.is.uno.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameRoomService {
    private final GameRoomRepository gameRoomRepository;
    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;

    public GameRoom findById(Long id) {
        return gameRoomRepository.findById(id).orElseThrow(() -> new GameRoomNotFoundException(
                String.format("Game room %s not found", id)
        ));
    }

    public void createGameRoom(GameRoomDTO gameRoomDTO, User owner) {
        GameRoom gameRoom = GameRoom.builder()
                .roomName(gameRoomDTO.getRoomName())
                .maxPlayers(gameRoomDTO.getMaxPlayers())
                .owner(owner)
                .build();
        if (gameRoomDTO.getPassword() != null) {
            gameRoom.setPassword(passwordEncoder.encode(gameRoomDTO.getPassword()));
        }

        gameRoom = gameRoomRepository.save(gameRoom);
    }

    public void joinGameRoom(JoinGameRoomDTO joinGameRoomDTO, User user) {
        GameRoom gameRoom = findById(joinGameRoomDTO.getRoomId());
        if (gameRoom.getPassword() != null &&
            !passwordEncoder.matches(joinGameRoomDTO.getPassword(), gameRoom.getPassword())) {
            throw new ForbiddenException("Incorrect password");
        }

        Player player = Player.builder()
                .inGameName(joinGameRoomDTO.getInGameName() != null ? joinGameRoomDTO.getInGameName() : user.getUsername())
                .user(user)
                .currentRoom(gameRoom)
                .build();

        playerRepository.save(player);
    }

    public List<GameRoomDTO> getAllGameRooms() {
        List<GameRoom> gameRooms = gameRoomRepository.findAll();
        return gameRooms.stream()
                .map(this::toGameRoomDTO)
                .collect(Collectors.toList());
    }

    private GameRoomDTO toGameRoomDTO(GameRoom gameRoom) {
        return GameRoomDTO.builder()
                .roomName(gameRoom.getRoomName())
                .password(gameRoom.getPassword())
                .maxPlayers(gameRoom.getMaxPlayers())
                .owner(gameRoom.getOwner().getUsername())
                .build();
    }
}
