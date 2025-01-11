package com.is.uno.core;

import com.is.uno.service.DeckService;
import com.is.uno.service.GameRoomService;
import com.is.uno.service.MessageService;
import com.is.uno.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class GameCoreProvider {

    private final SimpMessagingTemplate messagingTemplate;
    private final PlayerService playerService;
    private final GameRoomService gameRoomService;
    private final DeckService deckService;
    private final MessageService messageService;

    // ID -> game
    private final Map<Long, GameCore> gameCoreIds = new ConcurrentHashMap<>();
    // UUID -> game
    private final Map<UUID, GameCore> gameCoreUuids = new ConcurrentHashMap<>();

    public GameCore provideGameCore(Long roomId) {
        return gameCoreIds.computeIfAbsent(roomId, id -> {
            var gameCore = new GameCore(
                    id,
                    messagingTemplate,
                    gameRoomService,
                    messageService,
                    playerService,
                    deckService
            );
            gameCore.init();
            gameCoreUuids.put(gameCore.getUuid(), gameCore);
            return gameCore;
        });
    }

    public GameCore provideGameCore(UUID gameUUID) {
        if (!gameCoreUuids.containsKey(gameUUID)) {
            throw new IllegalArgumentException("Invalid gameUUID: " + gameUUID);
        }
        return gameCoreUuids.get(gameUUID);
    }

    public void destroyGameCore(GameCore gameCore) {
        gameCoreIds.remove(gameCore.getRoomId());
        gameCoreUuids.remove(gameCore.getUuid());
        gameCore.destroy();
    }

}

