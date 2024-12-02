package com.is.uno.socket;

import com.is.uno.core.GameCore;
import com.is.uno.dto.packet.*;
import com.is.uno.model.GameRoom;
import com.is.uno.model.Player;
import com.is.uno.model.User;
import com.is.uno.service.MessageService;
import com.is.uno.service.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
//@Scope("session")
@RequiredArgsConstructor
public class PacketHandler {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    private final PlayerService playerService;
    private GameCore game = new GameCore();
    @Setter
    private GameRoom room;

    public void sendPacketToAllPlayers(Packet packet) {
        messagingTemplate.convertAndSend("/topic/updates/" + room.getId(), packet);
    }

    public void handleTextPacket(TextPacket packet, User user) {
        if (user != null) {
            packet.setSender(user.getUsername());
        }
        else packet.setSender("Unknown");
        sendPacketToAllPlayers(packet);
    }

    public void handleActionPacket(ActionPacket packet, User user) {
        Player player = playerService.findByRoomAndUserOrCreate(room, user);
        var actionPacket = new PlayerActionPacket();
        actionPacket.setAction(packet.getAction());
        actionPacket.setPlayerId(player.getId());
        switch (packet.getAction()) {
            case READY -> {
                boolean gameStarted = game.onPlayerReady(player.getId());
                sendPacketToAllPlayers(actionPacket);
                if (gameStarted) {
                    var gameStartPacket = new ActionPacket();
                    gameStartPacket.setAction(Action.GAME_START);
                    sendPacketToAllPlayers(gameStartPacket);
                }
            }
            default -> {}
        }
    }

}
