package com.is.uno.core;

import com.is.uno.dto.api.CardDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameState {

    private CardDTO currentCard;
    private boolean orderReversed = false;
    private GamePlayer currentPlayer;
    private CardDeck deck;

    public void reverseOrder() {
        orderReversed = !orderReversed;
    }

}
