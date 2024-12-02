package com.is.uno.core;

import com.is.uno.dto.packet.Action;
import com.is.uno.dto.packet.ActionPacket;
import com.is.uno.dto.packet.PlayerJoinPacket;
import com.is.uno.model.GameRoom;
import com.is.uno.model.User;
import com.is.uno.service.GameRoomService;
import com.is.uno.service.MessageService;
import com.is.uno.service.PlayerService;
import com.is.uno.socket.PacketHandler;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class GameCore {

    private final Long roomId;
    private final SimpMessagingTemplate messagingTemplate;
    private final GameRoomService gameRoomService;
    private final MessageService messageService;
    private final PlayerService playerService;

    @Getter
    private PacketHandler packetHandler;
    private GameRoom room;
    private GameState state;
    // username -> player
    private Map<String, GamePlayer> players = new ConcurrentHashMap<>();

    void init() {
        room = gameRoomService.findById(roomId);
        packetHandler = new PacketHandler(roomId, messagingTemplate, this);
    }

    public GamePlayer getPlayerByUser(User user) {
        if (players.containsKey(user.getUsername())) {
            return players.get(user.getUsername());
        }
        GamePlayer player = new GamePlayer(
                playerService.findByRoomAndUserOrCreate(room, user),
                packetHandler);
        players.put(player.getUsername(), player);
        onPlayerJoin(player);
        return player;
    }

    public void onPlayerJoin(GamePlayer player) {
        var pkt = new PlayerJoinPacket();
        pkt.setUsername(player.getUsername());
        pkt.setInGameName(player.getInGameName());
        packetHandler.sendPacketToAllPlayers(pkt);
    }

    public void onPlayerReady(GamePlayer player) {
        player.onReady();
        boolean allReady = players.size() >= 2;
        for (var pl : players.values()) {
            allReady &= pl.isReady();
        }
        if (allReady && state == null) {
            startGame();
        }
    }

    public void startGame() {
        state = new GameState();
        packetHandler.sendPacketToAllPlayers(ActionPacket.create(Action.GAME_START));

    }

}
