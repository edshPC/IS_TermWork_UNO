package com.is.uno.socket;

import com.is.uno.core.GameCore;
import com.is.uno.core.GamePlayer;
import com.is.uno.dto.packet.*;
import com.is.uno.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@RequiredArgsConstructor
public class PacketHandler {

    private final SimpMessagingTemplate messagingTemplate;
    private final GameCore game;

    public void sendPacketToAllPlayers(Packet packet) {
        messagingTemplate.convertAndSend("/topic/game/" + game.getUuid(), packet);
    }

    public void sendPacketToPlayer(Packet packet, GamePlayer player) {
        messagingTemplate.convertAndSend("/topic/private/" + player.getUuid(), packet);
    }

    public void handle(Packet packet, User user) {
        GamePlayer player = game.getPlayerByUser(user);
        try {
            switch (packet.getType()) {
                case TEXT_PACKET -> handle((TextPacket) packet, player);
                case ACTION_PACKET -> handle((ActionPacket) packet, player);
                case PUT_CARD_PACKET -> handle((PutCardPacket) packet, player);
                default -> throw new IllegalArgumentException("Invalid packet type");
            }
        } catch (Exception e) {
            sendPacketToPlayer(TextPacket.createSystem(e.getMessage()), player);
        }
    }

    public void handle(TextPacket packet, GamePlayer player) {
        packet.setSender(player.getInGameName());
        sendPacketToAllPlayers(packet);
        game.saveMessage(player, packet.getText());
    }

    public void handle(ActionPacket packet, GamePlayer player) {
        switch (packet.getAction()) {
            case JOIN -> game.onPlayerJoin(player);
            case READY -> game.onPlayerReady(player);
            case LEAVE -> game.onPlayerLeave(player);
            case CALL_UNO -> game.onPlayerCallUNO(player);
            case TAKE_CARD -> game.onPlayerTakeCard(player);
            default -> throw new IllegalArgumentException("Invalid action type");
        }
    }

    public void handle(PutCardPacket packet, GamePlayer player) {
        game.onPlayerPutCard(player, player.getCard(packet.getCardId()), packet.getNewColor());
    }

}
