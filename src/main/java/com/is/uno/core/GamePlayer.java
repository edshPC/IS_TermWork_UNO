package com.is.uno.core;

import com.is.uno.dto.api.CardDTO;
import com.is.uno.dto.packet.Action;
import com.is.uno.dto.packet.Packet;
import com.is.uno.dto.packet.PlayerActionPacket;
import com.is.uno.model.Player;
import com.is.uno.socket.PacketHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class GamePlayer extends Player {
    private final Player player;
    private final PacketHandler packetHandler;

    @Getter
    private final UUID uuid = UUID.randomUUID();
    private final Map<Long, CardDTO> cards = new HashMap<>();

    @Getter
    private boolean ready = false;
    private boolean UNOCalled = false;

    public PlayerActionPacket getActionPacket(Action action) {
        PlayerActionPacket packet = new PlayerActionPacket();
        packet.setAction(action);
        packet.setUsername(getUsername());
        return packet;
    }

    public void sendPacket(Packet packet) {
        packetHandler.sendPacketToPlayer(packet, this);
    }

    public void onReady() {
        ready = !ready;
        packetHandler.sendPacketToAllPlayers(getActionPacket(Action.READY));
    }

    public boolean hasCard(Long id) {
        return cards.containsKey(id);
    }

    public CardDTO removeCard(Long id) {
        return cards.remove(id);
    }

    public void addCard(CardDTO card) {
        cards.put(card.getId(), card);
    }

    public void callUNO() {
        if (UNOCalled) throw new IllegalStateException("Вы уже сказали UNO");
        if (cards.size() != 2)
            throw new IllegalStateException("Вы можете говорить UNO только перед предпоследним ходом");
        UNOCalled = true;
    }

    @Override
    public String getInGameName() {
        return player.getInGameName();
    }

    public String getUsername() {
        return player.getUser().getUsername();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof GamePlayer other_player &&
               getUsername().equals(other_player.getUsername());
    }

}
