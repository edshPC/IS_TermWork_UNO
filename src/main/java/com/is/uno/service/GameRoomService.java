package com.is.uno.service;

import com.is.uno.core.GameCore;
import com.is.uno.core.GameCoreProvider;
import com.is.uno.core.GamePlayer;
import com.is.uno.core.UserEvent;
import com.is.uno.dao.GameRepository;
import com.is.uno.dao.GameRoomRepository;
import com.is.uno.dao.GameScoreRepository;
import com.is.uno.dao.UserRepository;
import com.is.uno.dto.api.*;
import com.is.uno.exception.ForbiddenException;
import com.is.uno.exception.GameRoomNotFoundException;
import com.is.uno.model.Game;
import com.is.uno.model.GameRoom;
import com.is.uno.model.GameScore;
import com.is.uno.model.User;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameRoomService {
    private final GameRoomRepository gameRoomRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final StatisticsService statisticsService;
    private final GameScoreRepository gameScoreRepository;
    private final GameRepository gameRepository;

    @Setter(onMethod_ = {@Autowired, @Lazy})
    private GameCoreProvider gameCoreProvider;
    private ApplicationEventPublisher applicationEventPublisher;

    public GameRoom findById(Long id) {
        return gameRoomRepository.findById(id).orElseThrow(() -> new GameRoomNotFoundException(
                String.format("Game room %s not found", id)
        ));
    }

    public JoinRoomResponse createGameRoom(CreateGameRoomDTO createGameRoomDTO, User owner) {
        if (createGameRoomDTO.getMaxPlayers() < 2) {
            throw new IllegalArgumentException("Минимальное количество игроков: 2");
        }

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
        applicationEventPublisher.publishEvent(new UserEvent(owner.getUsername(), UserEvent.Type.CREATE_ROOM));
        return joinGameRoom(JoinGameRoomDTO.builder()
                .roomId(gameRoom.getId())
                .password(createGameRoomDTO.getPassword())
                .build(), owner);
    }

    public JoinRoomResponse joinGameRoom(JoinGameRoomDTO joinGameRoomDTO, User user) {
        GameRoom gameRoom = findById(joinGameRoomDTO.getRoomId());
        if (gameRoom.getPassword() != null &&
                !passwordEncoder.matches(joinGameRoomDTO.getPassword(), gameRoom.getPassword())) {
            throw new ForbiddenException("Неверный пароль комнаты");
        }

        if (joinGameRoomDTO.getInGameName() != null) {
            user.setInGameName(joinGameRoomDTO.getInGameName());
        }
      // playerRepository.save(user);

        long playerCount = userService.countPlayersInRoom(gameRoom);
        if (playerCount > gameRoom.getMaxPlayers()) {
          //  userRepository.delete(user);
            throw new ForbiddenException("Комната заполнена");
        }
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
                .filter(GameRoom::getVisible)
                .map(this::toGameRoomDTO)
                .collect(Collectors.toList());
    }

    public LinkedList<GameStatDTO> onSingleGameOver(Game game, List<GameScore> scores) {
        gameRepository.save(game);
        LinkedList<GameStatDTO> stats = new LinkedList<>();
        for (var score : scores) {
            stats.add(GameStatDTO.builder()
                    .username(score.getUser().getUsername())
                    .score(score.getScore())
                    .totalScore(
                            score.getScore() +
                                    userService.calculateTotalScore(score.getUser())
                    )
                    .build());
            statisticsService.updatePlayerStatistics(score);
            gameScoreRepository.save(score);
        }
        stats.sort(Comparator.comparingLong(GameStatDTO::getTotalScore));
        if (game.getRoom().getMaxScore() > 0 &&
                stats.getLast().getTotalScore() >= game.getRoom().getMaxScore()) {
            game.getRoom().setVisible(false);
            gameRoomRepository.save(game.getRoom());
        }
        return stats;
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

    @Autowired
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
