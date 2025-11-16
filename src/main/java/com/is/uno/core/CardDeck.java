package com.is.uno.core;

import com.is.uno.config.Card;
import com.is.uno.config.Deck;
import com.is.uno.dto.api.CardDTO;
import com.is.uno.model.Type;

import java.util.concurrent.atomic.AtomicLong;

public class CardDeck {

    private final WeightedRandomBag<CardDTO> cards = new WeightedRandomBag<>();
    private final AtomicLong lastCardId = new AtomicLong(0);

    public void fillDeck(Deck deck) {
        for (Card card : deck.getCards()) {
            cards.addEntry(CardDTO.builder()
                            .type(card.getType())
                            .color(card.getColor())
                            .value(card.getValue())
                            .build(),
                    card.getWeight());
        }
    }

    public CardDTO takeCard() {
        var card = cards.get().clone();
        card.setId(lastCardId.incrementAndGet());
        return card;
    }

    public CardDTO takeNumberCard() {
        var card = takeCard();
        while (card.getType() != Type.NUMBER) card = takeCard();
        return card;
    }

}
