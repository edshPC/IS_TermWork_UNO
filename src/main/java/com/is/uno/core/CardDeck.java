package com.is.uno.core;

import com.is.uno.dto.api.CardDTO;
import com.is.uno.model.Deck;
import com.is.uno.model.Type;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class CardDeck {

    private final WeightedRandomBag<CardDTO> deck = new WeightedRandomBag<>();
    private final AtomicLong lastCardId = new AtomicLong(0);

    public void fillDeck(List<Deck> deckDB) {
        for (Deck d : deckDB) {
            deck.addEntry(CardDTO.builder()
                            .type(d.getCard().getType_of_card())
                            .color(d.getCard().getColor_of_card())
                            .value(d.getCard().getValue())
                            .build(),
                    d.getWeight());
        }
    }

    public CardDTO takeCard() {
        var card = deck.get().clone();
        card.setId(lastCardId.incrementAndGet());
        return card;
    }

    public CardDTO takeNumberCard() {
        var card = takeCard();
        while (card.getType() != Type.NUMBER) card = takeCard();
        return card;
    }

}
