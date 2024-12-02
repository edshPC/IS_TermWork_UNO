package com.is.uno.core;

import com.is.uno.service.GameRoomService;
import com.is.uno.service.MessageService;
import com.is.uno.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class GameCoreProvider {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    private final PlayerService playerService;
    private final GameRoomService gameRoomService;

    // ID -> game
    private final Map<Long, GameCore> gameCores = new ConcurrentHashMap<>();

    public GameCore provideGameCore(Long roomId) {
        return gameCores.computeIfAbsent(roomId, id -> {
            var gameCore = new GameCore(
                    id,
                    messagingTemplate,
                    gameRoomService,
                    messageService,
                    playerService
            );
            gameCore.init();
            return gameCore;
        });
    }
}

