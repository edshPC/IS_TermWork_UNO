package com.is.uno.service;

import com.is.uno.core.GameCore;
import com.is.uno.core.GameCoreProvider;
import com.is.uno.core.GamePlayer;
import com.is.uno.dao.GameRoomRepository;
import com.is.uno.dao.PlayerRepository;
import com.is.uno.dto.api.CreateGameRoomDTO;
import com.is.uno.dto.api.GameRoomDTO;
import com.is.uno.dto.api.JoinGameRoomDTO;
import com.is.uno.dto.api.JoinRoomResponse;
import com.is.uno.exception.ForbiddenException;
import com.is.uno.exception.GameRoomNotFoundException;
import com.is.uno.model.GameRoom;
import com.is.uno.model.Player;
import com.is.uno.model.User;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
    private final PlayerService playerService;

    @Setter(onMethod_ = {@Autowired, @Lazy})
    private GameCoreProvider gameCoreProvider;

    public GameRoom findById(Long id) {
        return gameRoomRepository.findById(id).orElseThrow(() -> new GameRoomNotFoundException(
                String.format("Game room %s not found", id)
        ));
    }

    public JoinRoomResponse createGameRoom(CreateGameRoomDTO createGameRoomDTO, User owner) {
        GameRoom gameRoom = GameRoom.builder()
                .roomName(createGameRoomDTO.getRoomName())
                .maxPlayers(createGameRoomDTO.getMaxPlayers())
                .maxScore(createGameRoomDTO.getMaxScore())
                .owner(owner)
                .visible(true)
                .build();
        if (createGameRoomDTO.getPassword() != null && !createGameRoomDTO.getPassword().isEmpty()) {
            gameRoom.setPassword(passwordEncoder.encode(createGameRoomDTO.getPassword()));
        }

        gameRoom = gameRoomRepository.save(gameRoom);
        return joinGameRoom(JoinGameRoomDTO.builder()
                .roomId(gameRoom.getId())
                .password(createGameRoomDTO.getPassword())
                .build(), owner);
    }

    public JoinRoomResponse joinGameRoom(JoinGameRoomDTO joinGameRoomDTO, User user) {
        GameRoom gameRoom = findById(joinGameRoomDTO.getRoomId());
        if (gameRoom.getPassword() != null &&
            !passwordEncoder.matches(joinGameRoomDTO.getPassword(), gameRoom.getPassword())) {
            throw new ForbiddenException("Incorrect password");
        }
        if (!gameRoom.getVisible()) throw new ForbiddenException("The room is full");

        Player player = playerService.findByRoomAndUserOrCreate(gameRoom, user);
        if (joinGameRoomDTO.getInGameName() != null) {
            player.setInGameName(joinGameRoomDTO.getInGameName());
        }
        playerRepository.save(player);

        long playerCount = playerService.countPlayersInRoom(gameRoom);
        if (playerCount >= gameRoom.getMaxPlayers()) {
            gameRoom.setVisible(false);
            gameRoomRepository.save(gameRoom);
        }

        GameCore game = gameCoreProvider.provideGameCore(gameRoom.getId());
        GamePlayer gamePlayer = game.getPlayerByUser(user);

        return JoinRoomResponse.builder()
                .roomId(gameRoom.getId())
                .gameUUID(game.getUuid())
                .privateUUID(gamePlayer.getUuid())
                .build();
    }

    public List<GameRoomDTO> getAllGameRooms() {
        List<GameRoom> gameRooms = gameRoomRepository.findAll();
        return gameRooms.stream()
                .map(this::toGameRoomDTO)
                .collect(Collectors.toList());
    }

    private GameRoomDTO toGameRoomDTO(GameRoom gameRoom) {
        return GameRoomDTO.builder()
                .id(gameRoom.getId())
                .roomName(gameRoom.getRoomName())
                .password(gameRoom.getPassword())
                .visible(gameRoom.getVisible())
                .maxPlayers(gameRoom.getMaxPlayers())
                .maxScore(gameRoom.getMaxScore())
                .owner(gameRoom.getOwner().getUsername())
                .build();
    }
}
