package com.is.uno.core;

import com.is.uno.dto.api.CardDTO;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class GamePlayer {
    Map<Long, CardDTO> cards = new HashMap<>();

    @Getter
    boolean ready = false;

    public boolean onReady() {
        ready = !ready;
        return ready;
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

}
