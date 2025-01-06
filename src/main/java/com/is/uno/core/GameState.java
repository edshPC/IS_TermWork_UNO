package com.is.uno.core;

import com.is.uno.dto.api.CardDTO;
import lombok.Getter;

@Getter
public class GameState {

    private CardDTO currentCard;
    private boolean orderReversed;
    private GamePlayer currentPlayer;
    private CardDeck deck;

}
