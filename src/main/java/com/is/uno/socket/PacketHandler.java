package com.is.uno.socket;

import com.is.uno.core.GameCore;
import com.is.uno.core.GamePlayer;
import com.is.uno.dto.packet.*;
import com.is.uno.model.Player;
import com.is.uno.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@RequiredArgsConstructor
public class PacketHandler {
    private final double idid = Math.random();

    private final Long roomId;
    private final SimpMessagingTemplate messagingTemplate;
    private final GameCore game;

    public void sendPacketToAllPlayers(Packet packet) {
        messagingTemplate.convertAndSend("/topic/room/" + roomId, packet);
    }

    public void sendPacketToPlayer(Packet packet, GamePlayer player) {
        messagingTemplate.convertAndSend("/topic/player/" + player.getUuid(), packet);
    }

    public void handle(Packet packet, User user) {
        GamePlayer player = game.getPlayerByUser(user);
        switch (packet.getType()) {
            case TEXT_PACKET -> handle((TextPacket) packet, player);
            case ACTION_PACKET -> handle((ActionPacket) packet, player);
            default -> throw new IllegalArgumentException("Invalid packet type");
        }
    }

    public void handle(TextPacket packet, GamePlayer player) {
        packet.setSender(player.getInGameName());
        sendPacketToAllPlayers(packet);
    }

    public void handle(ActionPacket packet, GamePlayer player) {
        switch (packet.getAction()) {
            case READY -> game.onPlayerReady(player);
            case LEAVE -> game.onPlayerLeave(player);
            case CALL_UNO -> game.onPlayerCallUNO(player);
            case TAKE_CARD -> game.onPlayerTakeCard(player);
            default -> throw new IllegalArgumentException("Invalid action type");
        }
    }

}
