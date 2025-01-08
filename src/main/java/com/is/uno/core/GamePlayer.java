package com.is.uno.core;

import com.is.uno.dto.api.CardDTO;
import com.is.uno.dto.packet.Action;
import com.is.uno.dto.packet.PlayerActionPacket;
import com.is.uno.model.Player;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class GamePlayer extends Player {
    @Getter
    private final Player player;

    @Getter
    private final UUID uuid = UUID.randomUUID();
    private final Map<Long, CardDTO> cards = new HashMap<>();

    @Getter
    private boolean ready = false;
    @Getter
    @Setter
    private boolean UNOCalled = false;

    public PlayerActionPacket getActionPacket(Action action) {
        PlayerActionPacket packet = new PlayerActionPacket();
        packet.setAction(action);
        packet.setUsername(getUsername());
        return packet;
    }

    public void onReady() {
        ready = !ready;
    }

    public int getCardCount() {
        return cards.size();
    }

    public long getTotalCardScore() {
        long sum = 0;
        for (var card : cards.values()) {
            sum += card.getValue();
        }
        return sum;
    }

    public boolean hasCard(Long id) {
        return cards.containsKey(id);
    }

    public CardDTO getCard(Long id) {
        return cards.get(id);
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

    public void reset() {
        cards.clear();
        UNOCalled = false;
        ready = false;
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
