package com.is.uno.core;

import com.is.uno.dto.api.CardDTO;
import com.is.uno.dto.packet.Action;
import com.is.uno.dto.packet.PlayerActionPacket;
import com.is.uno.model.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class GamePlayer {
    @Getter
    private final User user;

    @Getter
    private final UUID uuid = UUID.randomUUID();
    private final Map<Long, CardDTO> cards = new ConcurrentHashMap<>();

    @Getter
    @Setter
    private boolean loaded = false;
    @Getter
    private boolean ready = false;
    @Getter
    @Setter
    private boolean UNOCalled = false;
    @Getter
    @Setter
    private boolean cardTaken = false;

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
            throw new IllegalStateException("Вы не можете сказать UNO сейчас");
        UNOCalled = true;
    }

    public void reset() {
        cards.clear();
        UNOCalled = false;
        ready = false;
    }

    public boolean canPlaceCardOn(CardDTO card) {
        for (var c : cards.values()) {
            if (c.canPlaceOn(card)) return true;
        }
        return false;
    }

    public String getInGameName() {
        return user.getInGameName() != null ? user.getInGameName() : user.getUsername();
    }

    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof GamePlayer other_player &&
               getUsername().equals(other_player.getUsername());
    }

}
