package com.is.uno.core;

import com.is.uno.dto.api.CardDTO;
import com.is.uno.model.Deck;
import com.is.uno.model.Type;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class CardDeck {

    private Stack<CardDTO> deck;

    public void fillDeck(List<Deck> deckDB) {
        LinkedList<CardDTO> cards = new LinkedList<>();
        long lastCardId = 0;

        for (Deck d : deckDB) {
            for (int i = 0; i < d.getWeight(); i++)
                cards.add(CardDTO.builder()
                        .id(++lastCardId)
                        .type_of_card(d.getCard().getType_of_card())
                        .color_of_card(d.getCard().getColor_of_card())
                        .value(d.getCard().getValue())
                        .build());
        }

        do {
            deck = new Stack<>();
            while(!cards.isEmpty()) {
                int totalCards = cards.size();

                Random random = new Random();
                int pos = (Math.abs(random.nextInt())) % totalCards;

                CardDTO randomCard = cards.remove(pos);
                deck.add(randomCard);
            }
        } while (deck.peek().getType_of_card() != Type.NUMBER);
    }

    public CardDTO takeCard() {
        return deck.pop();
    }

}
